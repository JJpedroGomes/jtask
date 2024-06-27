package com.jjpedrogomes.controller.auth;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.jjpedrogomes.model.user.Email;
import com.jjpedrogomes.model.user.Password;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.util.JpaUtil;
import com.jjpedrogomes.user.UserDaoTest;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LoginServletTest {
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	@BeforeEach
	void setUp() {
		this.request = mock(HttpServletRequest.class);
		this.response = mock(HttpServletResponse.class);
	}
	
	@Test
	void sucessfullLogin() throws ServletException, IOException {
		// Arrange
		String email = "email@email.com";
		String password = "a1b2c3d4";
		HttpSession session = mock(HttpSession.class);
		
		User user = new User("Matthew McConaughey", new Email(email), new Password(password), LocalDate.now());
        UserDaoTest.persistUser(user);
		
		when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getSession()).thenReturn(session);
        
        EntityManager entityManagerForMock = JpaUtil.getEntityManager();
        when(request.getAttribute("entityManager")).thenReturn(entityManagerForMock);
		// Act
        LoginServlet loginServlet = new LoginServlet();
        loginServlet.doPost(request, response);
		// Assert
        verify(session).setAttribute("user", email);
        verify(session).setAttribute("displayName", user.getName());
        verify(session).setMaxInactiveInterval(30 * 60);
        verify(response).sendRedirect(request.getContextPath() + "/main");
	}
	
	@Test
	void failedLogin() throws ServletException, IOException {
		// Arrange
		String email = "otheremail@email.com";
		String password = "a1b2c3d4";
		String wrongPassord = "a1b2c3d5";
		
        User user = new User("Anne Hathaway", new Email(email), new Password(wrongPassord), LocalDate.now());
        UserDaoTest.persistUser(user);
		
		when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);	
        
        EntityManager entityManagerForMock = JpaUtil.getEntityManager();
        when(request.getAttribute("entityManager")).thenReturn(entityManagerForMock);
        // Act
        LoginServlet loginServlet = new LoginServlet();
        loginServlet.doPost(request, response);
        
        // Assert
        verify(response).sendRedirect(request.getContextPath() + "/pages/login.jsp?error=Invalid credentials");
	}
}
