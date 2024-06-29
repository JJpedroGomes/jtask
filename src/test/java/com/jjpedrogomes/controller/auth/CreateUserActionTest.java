package com.jjpedrogomes.controller.auth;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.jjpedrogomes.controller.util.ClientResponseHandlerImpl;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.repository.user.UserDaoImpl;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreateUserActionTest {
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private UserDao<User> userDao;
	private ClientResponseHandlerImpl clientResponseHandler;
	private CreateUserAction createUserAction;
	private String nameParam = "test";
	private String emailParam = "email@email.com";
	private String passwordParam = "a1b2c3d4";
	private String birthDateParam = LocalDate.of(1974, 9, 27).toString();
	
	@BeforeEach
	void setUp() {
		this.request = mock(HttpServletRequest.class);
		this.response = mock(HttpServletResponse.class);
		this.userDao = mock(UserDaoImpl.class);
		this.clientResponseHandler = new ClientResponseHandlerImpl(response);
		this.createUserAction = new CreateUserAction(userDao, clientResponseHandler);
	}

	@Test
	void create_user_with_invalid_param() throws IOException {
		// Arrange
		when(request.getParameter("name")).thenReturn(nameParam);
		when(request.getParameter("email")).thenReturn(null);
		when(request.getParameter("password")).thenReturn(passwordParam);
		when(request.getParameter("birthDate")).thenReturn(birthDateParam);
		when(response.getWriter()).thenReturn(mock(PrintWriter.class));
		// Act
		createUserAction.execute(request, response);
		// Assert
		verify(userDao, never()).save(any(User.class));
		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}
	
	@Test
	void create_user_sucessfully() throws IOException {
		// Arrange		
		when(request.getParameter("name")).thenReturn(nameParam);
		when(request.getParameter("email")).thenReturn(emailParam);
		when(request.getParameter("password")).thenReturn(passwordParam);
		when(request.getParameter("birthDate")).thenReturn(birthDateParam);
		when(response.getWriter()).thenReturn(mock(PrintWriter.class));
		 // Act
		createUserAction.execute(request, response);
		 // Assert
		verify(userDao).save(any(User.class));
		verify(response).setStatus(HttpServletResponse.SC_CREATED);
		assertThat(clientResponseHandler.getCurrentJson()).doesNotContain("error");
	}
	
	@Test
	void create_user_but_failed_to_persist() throws IOException {
		// Arrange
		when(request.getParameter("name")).thenReturn(nameParam);
		when(request.getParameter("email")).thenReturn(emailParam);
		when(request.getParameter("password")).thenReturn(passwordParam);
		when(request.getParameter("birthDate")).thenReturn(birthDateParam);
		when(response.getWriter()).thenReturn(mock(PrintWriter.class));
		doThrow(new RuntimeException("Simulated Error")).when(userDao).save(any(User.class));
		// Act
		createUserAction.execute(request, response);
		// Assert
		verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		assertThat(clientResponseHandler.getCurrentJson()).isEqualTo("{\"error\":" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR + "}");
	}
}
