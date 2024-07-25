package com.jjpedrogomes.controller.auth;

import java.time.LocalDate;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.controller.util.ErrorResponseUtil;
import com.jjpedrogomes.model.user.UserService;

public class CreateUserAction implements Action{
	
	private final UserService userService;
	private static final Logger logger = LogManager.getLogger(CreateUserAction.class);

	public CreateUserAction(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		String nameParam = request.getParameter("name");
		String emailParam = request.getParameter("email");
		String passwordParam = request.getParameter("password");
		String birthDateParam = request.getParameter("birthDate");
		
		try {
			logger.info("Saving user...");
			
			LocalDate birthDate = LocalDate.parse(birthDateParam);
			userService.createUser(nameParam, emailParam, passwordParam, birthDate);
			
			response.setStatus(HttpServletResponse.SC_CREATED);
		} catch (IllegalArgumentException e) {
			int errorCode = HttpServletResponse.SC_BAD_REQUEST;
			response.setStatus(errorCode);
			
			ErrorResponseUtil.handleErrorResponse(errorCode, e.getMessage(), response);
			throw e;
		} catch (PersistenceException e) {
			int errorCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			response.setStatus(errorCode);
			
			ErrorResponseUtil.handleErrorResponse(errorCode, e.getMessage(), response);
			throw e;
		}
	}
}
