package com.jjpedrogomes.controller.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthenticationFilter implements Filter{
	
	private final String[] loginRequiredUrls = {
			"/main", "/board.jsp"
	};

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpReponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false);
		
		String loginURI = httpRequest.getContextPath() + "/login";
		boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);
		boolean isLoginPage = httpRequest.getRequestURI().endsWith("login.jsp");
		boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
		
		if (isLoggedIn && (isLoginRequest || isLoginPage)) {
            // the user is already logged in and he's trying to login again
			httpReponse.sendRedirect(httpRequest.getContextPath() + "/main");
		} else if (!isLoggedIn && isLoginRequired(httpRequest)) {
			// the user is not logged in, and the requested page requires
            // authentication, then forward to the login page
			httpReponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
		} else {
			// for other requested pages that do not require authentication
            // or the user is already logged in, continue to the destination
            chain.doFilter(request, response);
		}
	}

	private boolean isLoginRequired(HttpServletRequest httpRequest) {
		String requestUrl = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
		
		for (String loginRequiredUrl : loginRequiredUrls) {
			if (requestUrl.contains(loginRequiredUrl)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void destroy() {}

}
