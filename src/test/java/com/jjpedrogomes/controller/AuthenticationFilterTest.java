package com.jjpedrogomes.controller;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.jjpedrogomes.controller.filter.AuthenticationFilter;
import com.jjpedrogomes.model.user.User;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthenticationFilterTest {
	
	private AuthenticationFilter authenticationFilter;
	private HttpServletRequest requestMock;
	private HttpServletResponse responseMock;
	private FilterChain filterChainMock;
	private final String contextPath = "/jtask";
	
	
	@BeforeEach
	void setUp() {
		this.authenticationFilter = new AuthenticationFilter();
		this.requestMock = mock(HttpServletRequest.class);
        this.responseMock = mock(HttpServletResponse.class);
        this.filterChainMock = mock(FilterChain.class);
	}

	@Test
	void user_is_loggedin_and_trying_to_login() throws IOException, ServletException {
		// Arrange
		HttpSession session = mock(HttpSession.class);
		when(requestMock.getSession(anyBoolean())).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(mock(User.class));
		when(requestMock.getContextPath()).thenReturn(contextPath);
		when(requestMock.getRequestURI()).thenReturn(contextPath + "/login");
		// Act
		authenticationFilter.doFilter(requestMock, responseMock, filterChainMock);
		// Assert
		verify(responseMock).sendRedirect(requestMock.getContextPath() + "/main");
		verify(filterChainMock, never()).doFilter(requestMock, responseMock);
	}
	
	@Test
	void user_is_loggedin_and_trying_to_acess_login_required_url() throws IOException, ServletException {
		// Arrange
		HttpSession session = mock(HttpSession.class);
		when(requestMock.getSession(anyBoolean())).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(mock(User.class));
		when(requestMock.getContextPath()).thenReturn(contextPath);
		when(requestMock.getRequestURI()).thenReturn(contextPath + "/main");
		// Act
		authenticationFilter.doFilter(requestMock, responseMock, filterChainMock);
		// Assert
		verify(responseMock, never()).sendRedirect(anyString());
		verify(filterChainMock).doFilter(requestMock, responseMock);
	}
	
	@Test
	void user_is_not_loggedin_and_trying_to_acess_login_required_url() throws IOException, ServletException {
		// Arrange
		when(requestMock.getSession(anyBoolean())).thenReturn(null);
		when(requestMock.getContextPath()).thenReturn(contextPath);
		when(requestMock.getRequestURI()).thenReturn(contextPath + "/main");
		// Act
		authenticationFilter.doFilter(requestMock, responseMock, filterChainMock);
		// Assert
		verify(responseMock).sendRedirect(requestMock.getContextPath() + "/login.jsp");
		verify(filterChainMock, never()).doFilter(requestMock, responseMock);
	}
	
	@Test
	void user_is_not_loggedin_and_trying_acess_url_that_dont_require_login() throws IOException, ServletException {
		// Arrange
		when(requestMock.getSession(anyBoolean())).thenReturn(null);
		when(requestMock.getContextPath()).thenReturn(contextPath);
		when(requestMock.getRequestURI()).thenReturn(contextPath + "/login.jsp");
		// Act
		authenticationFilter.doFilter(requestMock, responseMock, filterChainMock);
		// Assert
		verify(responseMock, never()).sendRedirect(anyString());
		verify(filterChainMock).doFilter(requestMock, responseMock);
	}
}
