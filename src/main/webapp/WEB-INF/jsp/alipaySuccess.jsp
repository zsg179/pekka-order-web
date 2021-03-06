<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
   <meta http-equiv="pragma" content="no-cache" />
   <meta http-equiv="cache-control" content="no-cache" />
   <meta http-equiv="expires" content="0" /> 
   <meta name="format-detection" content="telephone=no" />  
   <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" /> 
   <meta name="format-detection" content="telephone=no" />
   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
   <link type="text/css" rel="stylesheet" href="/css/base.css" />
   <link rel="stylesheet" type="text/css" href="/css/purchase.base.2012.css" />
   <link rel="stylesheet" type="text/css" href="/css/purchase.sop.css" />
   <title>支付成功页面 - 皮卡商城</title>
   <script type="text/javascript" src="/js/jquery-1.6.4.js"></script>
   <script type="text/javascript" src="/js/base-2011.js" charset="utf-8"></script>
   <script type="text/javascript" src="/js/jquery.cookie.js" charset="utf-8"></script>
   <script type="text/javascript" src="/js/taotao.js" charset="utf-8"></script>
</head> <body id="mainframe">
<!--shortcut start-->
<jsp:include page="commons/shortcut.jsp" />
<!--shortcut end-->
<div class="w" id="headers">
		<div id="logo"><a href="http://localhost:8082"><img alt="皮卡商城" src="/images/pekka-logo.jpg" width="150" height="62" alt="皮卡" title="返回皮卡商城首页" alt="返回皮卡商城首页"></a></div>
		<!-- <ul class="step" id="step3">
			<li class="fore1">1.我的购物车<b></b></li>
			<li class="fore2">2.填写核对订单信息<b></b></li>
			<li class="fore3">3.成功提交订单</li>
		</ul> -->
		<div class="clr"></div>
</div>
<div class="w" id="safeinfo"></div><!--父订单的ID-->
<div class="w main">
	<div class="m m3 msop">
        <div class="mt" id="success_tittle"><s class="icon-succ02"></s><h3 class="ftx-02">订单付款成功！</h3>
		</div>
		<div class="mc" id="success_detail">	
		    <ul class="list-order">
			    <li class="li-st">
					<%-- <div class="fore1">提交订单成功，请尽快付款！订单号：</div>${orderId } --%>
					<div class="fore2">在线支付：<strong class="ftx-01">${total_amount}元</strong></div>
					<div class="fore3">
					   	等待卖家发货
					</div>
					
				</li>
			</ul>
		<!-- 在线支付按钮  -->
				<div id="bookDiv"></div>
 					<!-- <p class="i-tips01">
				            	您的订单已经在处理中，发货后订单内容会显示承运人联系方式，如有必要您可以联系对方
             		</p> -->
		 </div>
		 <a href="http://localhost:8091/order/allOrders/${userId }.action">查看订单</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="http://localhost:8082">回首页</a>
	</div>
</div>
  <div class="w">
	<!-- links start -->
    <jsp:include page="commons/footer-links.jsp"></jsp:include>
    <!-- links end -->
</div><!-- footer end -->

<!-- <script type="text/javascript">
var orderId = ${orderId };
	function pay(){
		$.ajax({
			url:"/goAlipay.action",
			type: "POST",
	    	data: {"orderId":orderId},
	    	dataType: "json"
		})
	}
</script> -->
     </body> 
</html>

<%-- <html>

    <head>
        
    </head>
    
    <body>
        <h1 style="color: green;">购买成功</h1>
        <table>
        	<tr>
        		<td>
        			订单编号: ${out_trade_no }
        		</td>
        	</tr>
        		<td>
        			支付宝交易号: ${trade_no }
        		</td>
        	<tr>
        	</tr>
        		<td>
        			实付金额: ${total_amount }
        		</td>
        	<tr>
        	</tr>
        		<td>
        			购买产品：${productName }
        		</td>
        	</tr>
        </table>
    </body>
    
</html>
 --%>

