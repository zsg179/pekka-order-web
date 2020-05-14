package com.pekka.order.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.pekka.common.pojo.PekkaResult;
import com.pekka.common.util.CookieUtils;
import com.pekka.common.util.JsonUtils;
import com.pekka.order.alipayconfig.AlipayConfig;
import com.pekka.order.pojo.OrderInfo;
import com.pekka.order.service.OrderService;
import com.pekka.pojo.TbItem;
import com.pekka.pojo.TbOrder;
import com.pekka.pojo.TbOrderItem;
import com.pekka.pojo.TbReceivingAddress;
import com.pekka.pojo.TbUser;

/**
 * 订单确认页面处理
 * 
 * @author lenovo
 *
 */
@Controller
public class OrderCartController {

	final static Logger log = LoggerFactory.getLogger(OrderCartController.class);

	@Value("${CART_KEY}")
	private String CART_KEY;
	@Value("${CART_EXPIER}")
	private Integer CART_EXPIER;

	@Autowired
	private OrderService orderService;

	/**
	 * 展示订单确认页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request) {
		// 用户必须是登陆状态
		// 取用户id
		TbUser user = (TbUser) request.getAttribute("user");
		// 根据用户信息取收获地址列表
		TbReceivingAddress receiver = orderService.getReceiverByUserName(user.getUsername());
		receiver.setUsername(user.getUsername());// 防止是第一次请求，receiver内的值全空的情况
		// 把收获地址列表取出传递给页面
		request.setAttribute("receiver", receiver);
		// 传递用户id和用户名给页面
		request.setAttribute("userId", user.getId());
		request.setAttribute("userName", user.getUsername());
		// 从cookie中取购物车商品列表展示到页面
		List<TbItem> cartItemList = getCartItemList(request);
		request.setAttribute("cartList", cartItemList);
		return "order-cart";
	}

	private List<TbItem> getCartItemList(HttpServletRequest request) {
		// 从cookie中取购物车商品列表
		String json = CookieUtils.getCookieValue(request, CART_KEY, true);
		if (StringUtils.isBlank(json)) {
			// 如果没有内容,返回一个空的列表
			return new ArrayList<>();
		}
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}

	public void removeCartItem(OrderInfo orderInfo, HttpServletRequest request, HttpServletResponse response) {
		List<TbOrderItem> list = orderInfo.getOrderItems();
		// 获取购物车中的商品
		List<TbItem> itemList = getCartItemList(request);
		for (TbOrderItem tbOrderItem : list) {
			// 已购买商品的id
			String itemId = tbOrderItem.getItemId();
			Long LongItemId = Long.parseLong(itemId);
			for (TbItem tbItem : itemList) {
				if (LongItemId.equals((tbItem.getId()))) {
					itemList.remove(tbItem);
					break;
				}
			}
		}
		// 重新设置cookie
		CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(itemList), CART_EXPIER, true);

	}

	@RequestMapping(value = "/order/create", method = RequestMethod.POST)
	@ResponseBody
	public PekkaResult createOrder(OrderInfo orderInfo, HttpServletRequest request, HttpServletResponse response) {
		// 生成订单
		PekkaResult result = orderService.createOrder(orderInfo);
		if (result.getStatus() == 500) {
			return result;
		}
		orderInfo = (OrderInfo) result.getData();
		// 移除购物车中以购买的商品
		removeCartItem(orderInfo, request, response);
		// 存入session
		request.getSession().setAttribute("orderId", orderInfo.getOrderId());
		request.getSession().setAttribute("payment", orderInfo.getPayment());
		request.getSession().setAttribute("userId", orderInfo.getUserId());
		return result;
	}

	@RequestMapping(value = "/goAlipay", produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String goAlipay(String orderId, HttpServletRequest request, HttpServletRequest response) throws Exception {
		// 获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
				AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key,
				AlipayConfig.sign_type);

		// 设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AlipayConfig.return_url);
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

		TbOrder order = orderService.getOrderByOrderId(orderId);
		TbOrderItem orderItem = orderService.getOrderItemByOrderId(orderId);

		// 商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = orderId;
		// 付款金额，必填
		String total_amount = order.getPayment();
		// 订单名称，必填
		String subject = orderItem.getTitle();
		// 商品描述，可空
		String body = "";

		// 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
		String timeout_express = "1c";

		alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"," + "\"total_amount\":\"" + total_amount
				+ "\"," + "\"subject\":\"" + subject + "\"," + "\"body\":\"" + body + "\"," + "\"timeout_express\":\""
				+ timeout_express + "\"," + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

		String result = alipayClient.pageExecute(alipayRequest).getBody();
		// 调用成功，则处理业务逻辑
		return result;
	}

	/**
	 *
	 * @Title: AlipayController.java
	 * @Package com.sihai.controller
	 * @Description: 支付宝同步通知页面 Copyright: Copyright (c) 2017
	 *               Company:FURUIBOKE.SCIENCE.AND.TECHNOLOGY
	 *
	 * @author sihai
	 * @date 2017年8月23日 下午8:51:01
	 * @version V1.0
	 */
	@RequestMapping(value = "/alipayReturnNotice")
	public ModelAndView alipayReturnNotice(HttpServletRequest request, HttpServletRequest response) throws Exception {

		log.info("支付成功, 进入同步通知接口...");

		// 获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}

		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset,
				AlipayConfig.sign_type); // 调用SDK验证签名

		ModelAndView mv = new ModelAndView("alipaySuccess");
		// 商户订单号
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

		// ——请在这里编写您的程序（以下代码仅作参考）——
		if (signVerified) {
			// 商户订单号
			out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

			// 支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

			// 付款金额
			String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

			// 修改叮当状态，改为 支付成功，已付款; 同时新增支付流水
			/*
			 * orderService.updateOrderStatus(out_trade_no, trade_no,
			 * total_amount);
			 * 
			 * Orders order = orderService.getOrderById(out_trade_no); Product
			 * product = productService.getProductById(order.getProductId());
			 */
			// 修改订单状态为"已支付"。'状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭'
			orderService.updateOrderStatus(out_trade_no, 2);
			log.info("********************** 支付成功(支付宝同步通知) **********************");
			log.info("* 订单号: {}", out_trade_no);
			log.info("* 支付宝交易号: {}", trade_no);
			log.info("* 实付金额: {}", total_amount);
			// log.info("* 购买产品: {}", product.getName());
			log.info("***************************************************************");

			mv.addObject("out_trade_no", out_trade_no);
			mv.addObject("trade_no", trade_no);
			mv.addObject("total_amount", total_amount);
			// mv.addObject("productName", product.getName());

		} else {
			log.info("支付, 验签失败...");
			System.out.println("同步失败signVerified=" + signVerified);
		}

		return mv;
	}

	/**
	 *
	 * @Title: AlipayController.java
	 * @Package com.sihai.controller
	 * @Description: 支付宝异步 通知页面 Copyright: Copyright (c) 2017
	 *               Company:FURUIBOKE.SCIENCE.AND.TECHNOLOGY
	 *
	 * @author sihai
	 * @date 2017年8月23日 下午8:51:13
	 * @version V1.0
	 */
	@RequestMapping(value = "/alipayNotifyNotice")
	@ResponseBody
	public String alipayNotifyNotice(HttpServletRequest request, HttpServletRequest response) throws Exception {

		log.info("支付成功, 进入异步通知接口...");

		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}

		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset,
				AlipayConfig.sign_type); // 调用SDK验证签名

		// ——请在这里编写您的程序（以下代码仅作参考）——

		/*
		 * 实际验证过程建议商户务必添加以下校验： 1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
		 * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
		 * 3、校验通知中的seller_id（或者seller_email)
		 * 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
		 * 4、验证app_id是否为该商户本身。
		 */
		if (signVerified) {// 验证成功
			// 商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

			// 支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

			// 交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

			// 付款金额
			String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
			System.out.println("异步signVerified=" + signVerified + "\t" + out_trade_no);
			if (trade_status.equals("TRADE_FINISHED")) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 如果有做过处理，不执行商户的业务程序

				// 注意： 尚自习的订单没有退款功能, 这个条件判断是进不来的, 所以此处不必写代码
				// 退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
			} else if (trade_status.equals("TRADE_SUCCESS")) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 如果有做过处理，不执行商户的业务程序

				// 注意：
				// 付款完成后，支付宝系统发送该交易状态通知

				// 修改叮当状态，改为 支付成功，已付款; 同时新增支付流水
				/*
				 * orderService.updateOrderStatus(out_trade_no, trade_no,
				 * total_amount);
				 * 
				 * Orders order = orderService.getOrderById(out_trade_no);
				 * Product product =
				 * productService.getProductById(order.getProductId());
				 */
				// 修改订单状态为"已支付"。'状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭'
				orderService.updateOrderStatus(out_trade_no, 2);
				log.info("********************** 支付成功(支付宝异步通知) **********************");
				log.info("* 订单号: {}", out_trade_no);
				log.info("* 支付宝交易号: {}", trade_no);
				log.info("* 实付金额: {}", total_amount);
				// log.info("* 购买产品: {}", product.getName());
				log.info("***************************************************************");
			}
			log.info("支付成功...");

		} else {// 验证失败
			log.info("支付, 验签失败...");
			System.out.println("异步失败signVerified=" + signVerified);
		}

		return "success";
	}

	@RequestMapping(value = "/order/saveReceiver", method = RequestMethod.POST)
	@ResponseBody
	public PekkaResult saveReceiver(TbReceivingAddress receiver) {
		PekkaResult result = orderService.saveReceiver(receiver);
		return result;
	}

	@RequestMapping("/order/success")
	public String toSuccess() {
		return "success";
	}

	@RequestMapping("/order/allOrders/{userId}")
	public String showOrders(Model model, @PathVariable Long userId, HttpServletRequest request) {
		List<OrderInfo> allOrders = orderService.getAllOrders(userId);
		model.addAttribute("allOrders", allOrders);
		// model.addAttribute("userId", userId);
		request.getSession().setAttribute("userId", userId);
		return "orders";
	}

	@RequestMapping("/order/deleteOrder")
	@ResponseBody
	public PekkaResult deleteOrder(String orderId) {
		return orderService.deleteOrder(orderId);
	}

	@RequestMapping("/order/showOrdersByStatus/{userId}/{status}")
	public String showOrdersByStatus(Model model, @PathVariable Long userId, @PathVariable int status) {
		List<OrderInfo> ordertoPay = orderService.getOrderByStatus(userId, status);
		model.addAttribute("allOrders", ordertoPay);
		model.addAttribute("status", status);
		// model.addAttribute("userId", userId);
		return "orders";
	}

	@RequestMapping("/order/harvest")
	@ResponseBody
	public PekkaResult harvest(String orderId) {
		return orderService.harvest(orderId);
	}

	@RequestMapping("/order/searchOrder")
	public String searchOrder(String key, long userId, Model model) {
		List<OrderInfo> searchOrder = orderService.searchOrder(key);
		model.addAttribute("allOrders", searchOrder);
		model.addAttribute("userId", userId);
		return "orders";
	}
}
