package com.jjpedrogomes.controller.auth;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.controller.util.ClientResponseHandler;
import com.jjpedrogomes.model.shared.ModelErrorCode;
import com.jjpedrogomes.model.shared.ModelException;
import com.jjpedrogomes.model.user.Email;
import com.jjpedrogomes.model.user.Password;
import com.jjpedrogomes.model.user.User;

public class CreateUserAction implements Action{
	
	private final UserDao<User> userDao;
	private ClientResponseHandler clientResponseHandler;
	private static final Logger logger = LogManager.getLogger(CreateUserAction.class);

	public CreateUserAction(UserDao<User> userDao, ClientResponseHandler clientResponseHandler) {
		this.userDao = userDao;
		this.clientResponseHandler = clientResponseHandler;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		String nameParam = request.getParameter("name");
		String emailParam = request.getParameter("email");
		String passwordParam = request.getParameter("password");
		String birthDateParam = request.getParameter("birthDate");
		
		User user = createUser(response, nameParam, emailParam, passwordParam, birthDateParam);
		if (user == null) {
			return;
		}
		
		try {
			logger.info("Saving user...");
			userDao.save(user);
			response.setStatus(HttpServletResponse.SC_CREATED);	
			clientResponseHandler.createObjectJsonResponse(user);
		} catch (Exception e) {
			logger.error("Unexpected error saving user: {}", e.getMessage(), e);
            clientResponseHandler.createErrorJsonResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			clientResponseHandler.commitJsonToResponse();
		}
	}

	private User createUser(HttpServletResponse response, String nameParam, String emailParam, String passwordParam, String birthDateParam) {
		try {
			logger.info("Creating user...");
			Email email = new Email(emailParam);
			Password password = new Password(passwordParam);
			LocalDate birthDate = LocalDate.parse(birthDateParam);
			return new User(nameParam, email, password, birthDate);
		} catch (ModelException e) {		
			ModelErrorCode errorCode = e.getErrorCode();
			logger.error("Error creating user: {}", errorCode.getLogMessage(), e);
			
			clientResponseHandler.createErrorJsonResponse(errorCode.getCode());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		} catch (Exception e) {
			int errorCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			logger.error("Unexpected error creating user: {}", e.getMessage(), e);
			
			clientResponseHandler.createErrorJsonResponse(errorCode);
			response.setStatus(errorCode);	
            return null;
		}	
	}
}
