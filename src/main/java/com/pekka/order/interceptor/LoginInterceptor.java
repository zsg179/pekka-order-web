package com.pekka.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.pekka.common.pojo.PekkaResult;
import com.pekka.common.util.CookieUtils;
import com.pekka.pojo.TbUser;
import com.pekka.sso.service.UserService;

public class LoginInterceptor implements HandlerInterceptor {

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	@Value("${SSO_URL}")
	private String SSO_URL;

	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 执行handler之前执行
		// 1.从cookie中取token信息
		String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		// 2.如果取不到token，跳转到sso的登陆页面，需要把当前请求的url作为参数传递给sso，sso登陆成功后跳转回请求的页面
		if (StringUtils.isBlank(token)) {
			// 取当前请求的url
			StringBuffer requestURL = request.getRequestURL();
			response.sendRedirect(SSO_URL + "/page/login?url=" + requestURL);
			// 拦截
			return false;
		}
		// 3.取到token，调用sso系统的服务判断是否登陆
		PekkaResult PekkaResult = userService.getUserByToken(token);
		if (PekkaResult.getStatus() != 200) {
			// 未取到用户信息，即用户未登录，跳转到sso登陆页面
			// 取当前请求的url
			StringBuffer requestURL = request.getRequestURL();
			response.sendRedirect(SSO_URL + "/page/login?url=" + requestURL);
			// 拦截
			return false;
		}
		// 取到用户信息，放行
		TbUser user = (TbUser) PekkaResult.getData();
		request.setAttribute("user", user);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// 执行handler之后，ModelAndView之前
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 执行ModelAndView之后，异常处理
	}

}
