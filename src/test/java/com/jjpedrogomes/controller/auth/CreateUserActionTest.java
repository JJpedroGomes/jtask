package com.jjpedrogomes.controller.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.jjpedrogomes.controller.util.ClientResponseHandlerImpl;
import com.jjpedrogomes.model.shared.ModelError;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.user.UserDao;
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
		JsonObject json = clientResponseHandler.getCurrentJson();
		assertTrue(json.has("error"));
		assertThat(json.get("error").getAsInt()).isEqualTo(ModelError.INVALID_EMAIL.getCode());
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
		JsonObject json = clientResponseHandler.getCurrentJson();
//		assertTrue(json.has("id"));
		assertTrue(json.has("name"));
		assertTrue(json.has("email"));
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
		assertTrue(clientResponseHandler.getCurrentJson().has("error"));
	}
	
	@Test
	void create_user_but_email_alredy_taken() throws IOException {
		// Arrange
		when(request.getParameter("name")).thenReturn(nameParam);
		when(request.getParameter("email")).thenReturn(emailParam);
		when(request.getParameter("password")).thenReturn(passwordParam);
		when(request.getParameter("birthDate")).thenReturn(birthDateParam);
		when(response.getWriter()).thenReturn(mock(PrintWriter.class));
		when(userDao.getUserByEmail(emailParam)).thenReturn(Optional.of(new User()));
		// Act
		createUserAction.execute(request, response);
		// Assert
		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
		JsonObject json = clientResponseHandler.getCurrentJson();
		int error = json.get("error").getAsInt();
		String message = json.get("message").getAsString();
		assertThat(error).isEqualTo(ModelError.EMAIL_ALREADY_TAKEN.getCode());
		assertThat(message).isEqualTo(ModelError.EMAIL_ALREADY_TAKEN.getLogMessage());
	}
	
	
	
	
}
