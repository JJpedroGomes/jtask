package com.jjpedrogomes.controller.auth;

import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.controller.util.ClientResponseHandler;
import com.jjpedrogomes.controller.util.ClientResponseHandlerImpl;
import com.jjpedrogomes.model.user.UserService;

public class UpdateUserAction implements Action {
	
	private UserService userService;
	private static final Logger logger = LogManager.getLogger(UpdateUserAction.class);
	
	public UpdateUserAction(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		logger.info("Entering method execute() in UpdateUserAction");
		
		UserDto userDto = new UserDto();
		userDto.setName(request.getParameter("name"))
			.setEmail(request.getParameter("email"))
			.setPassword1(request.getParameter("password"))
			.setPassword2(request.getParameter("confirm_password"))
			.setBirthDate(request.getParameter("birthDate"));
		
		ClientResponseHandler clientResponseHandler = new ClientResponseHandlerImpl(response);
		clientResponseHandler.createJsonResponse();
		
		try {	
			logger.info("Updating user...");
			userService.updateUser(userDto);
		} catch (IllegalArgumentException e) {
			int errorCode = HttpServletResponse.SC_BAD_REQUEST;
			response.setStatus(errorCode);
			clientResponseHandler.setErrorCode(errorCode);
			clientResponseHandler.setMessage(e.getMessage());
			throw e;
		} catch (NoSuchElementException e) {
			int errorCode = HttpServletResponse.SC_NOT_FOUND;
			response.setStatus(errorCode);
			clientResponseHandler.setErrorCode(errorCode);
			clientResponseHandler.setMessage("No user found with the given email");
			throw e;
		}
	}
}
