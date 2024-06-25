package com.jjpedrogomes.controller.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class UserControllerTest {
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private EntityManager entityManager;
	
	@BeforeEach
	void setUp() {
		this.request = mock(HttpServletRequest.class);
		this.response = mock(HttpServletResponse.class);
		this.entityManager = mock(EntityManager.class);
	}

	@Test
	void doPost_with_create_user_action() throws Exception {
		// Arrange
		when(request.getAttribute("entityManager")).thenReturn(entityManager);
		when(request.getParameter("action")).thenReturn("CreateUser");
		when(response.getWriter()).thenReturn(mock(PrintWriter.class));
		when(request.getParameter("email")).thenReturn("email@email.com");
		when(request.getParameter("name")).thenReturn("test");
		when(request.getParameter("password")).thenReturn("a1b2c3d4");
		when(request.getParameter("birthDate")).thenReturn(LocalDate.of(1974, 9, 27).toString());
		
		UserController usrController = new UserController();
		
		// Act & Assert
        assertDoesNotThrow(() -> {
            usrController.doPost(request, response);
        });
		verify(response).setStatus(HttpServletResponse.SC_CREATED);
	}
	
    void doPost_with_invalid_action_parameter() {
		
	}
	
}
