package com.jjpedrogomes.controller.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.jjpedrogomes.model.user.UserService;
import com.jjpedrogomes.model.user.UserServiceFactory;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class UserControllerTest {
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private EntityManager entityManager;
	private EntityTransaction transaction;
	
	@BeforeEach
	void setUp() {
		this.request = mock(HttpServletRequest.class);
		this.response = mock(HttpServletResponse.class);
		this.entityManager = mock(EntityManager.class);
		this.transaction = mock(EntityTransaction.class);
	}

	@Test
	void doPost_with_create_user_action() throws Exception {
		// Arrange
		provideEntityManager();
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
	
	@Test
	void doPost_with_update_user_action() throws Exception {
		// Arrange
		UserController usrController = new UserController();
		UserService usrService = UserServiceFactory.getInstance();
		
		Method method = UserController.class.getDeclaredMethod("newInstance", String.class, UserService.class);
		method.setAccessible(true);
		// Act
		UpdateUserAction actionInstance = (UpdateUserAction) method.invoke(usrController, "UpdateUser", usrService);
		// Assert
		assertThat(actionInstance).isInstanceOf(UpdateUserAction.class);
	}
	
	@Test
    void doPost_with_invalid_action_parameter() throws Exception {
		// Arrange
		provideEntityManager();
		when(response.getWriter()).thenReturn(mock(PrintWriter.class));
		when(request.getParameter("action")).thenReturn("idk");
		UserController usrController = new UserController();
		// Act
		usrController.doPost(request, response);
		// Assert
		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}
	
	@Test
	void doGet_userDto_but_cant_find_user() throws Exception {
		// Arrange
		provideEntityManager();
		HttpSession session = mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn("email");
		UserController usrController = new UserController();
		// Act && Assert
		assertThrows(RuntimeException.class, () -> usrController.doGet(request, response));
	}
	
	private void provideEntityManager() {
		when(request.getAttribute("entityManager")).thenReturn(entityManager);
		when(entityManager.getTransaction()).thenReturn(transaction);
	}
}
