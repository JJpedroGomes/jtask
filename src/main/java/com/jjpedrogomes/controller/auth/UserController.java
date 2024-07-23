package com.jjpedrogomes.controller.auth;

import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.controller.util.ActionPathUtil;
import com.jjpedrogomes.controller.util.ClientResponseHandler;
import com.jjpedrogomes.controller.util.ClientResponseHandlerImpl;
import com.jjpedrogomes.controller.util.PathConstants;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.user.UserDao;
import com.jjpedrogomes.model.user.UserDaoFactory;
import com.jjpedrogomes.model.user.UserService;
import com.jjpedrogomes.model.user.UserServiceFactory;
import com.jjpedrogomes.repository.user.UserDaoImpl;

@WebServlet(
        name = "UserController",
        urlPatterns = "/user"
)
public class UserController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(UserController.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		logger.info("Entering method doPost() in UserController Servlet");
		
//		EntityManager entityManager = (EntityManager) request.getAttribute("entityManager");
//		UserDao<User> userDao = new UserDaoImpl(entityManager);
		UserDao<User> userDao = UserDaoFactory.getInstance();
		UserService userService = UserServiceFactory.getInstance();
		
		ClientResponseHandler clientResponseHandler = new ClientResponseHandlerImpl(response);
		
		String actionParam = request.getParameter("action");
		
		Action action;
		if(actionParam.equals("UpdateUser")) {
			action = newInstance(actionParam, userService);
		} else {
		    action = newInstance(actionParam, userDao, clientResponseHandler);
		}
		
		if (action == null) {
			int scBadRequest = HttpServletResponse.SC_BAD_REQUEST;
			response.setStatus(scBadRequest);
			clientResponseHandler.createJsonResponse().setErrorCode(scBadRequest);
			return;
		}
		
		action.execute(request, response);
		clientResponseHandler.commitJsonToResponse();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("Entering method doGet() in UserController Servlet");
		String email = (String) request.getSession().getAttribute("user");
		
		EntityManager entityManager = (EntityManager) request.getAttribute("entityManager");
		UserDao<User> userDao = new UserDaoImpl(entityManager);
		
		UserDto userDto = userDao.getUserByEmail(email)
			    .map(UserDto::new)
			    .orElseThrow(() -> new RuntimeException("User not found"));
		
		request.setAttribute("userDTO", userDto);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(PathConstants.ACCOUNT_DETAILS.getPath());
		dispatcher.forward(request,response);
	}
	
	private Action newInstance(String action, UserDao<User> userDao, ClientResponseHandler clientResponseHandler) {
		try {
			String qualifiedClassName = ActionPathUtil.getQualifiedClassName(PathConstants.USER, action);
			Constructor<?> constructor = Class.forName(qualifiedClassName).getConstructor(UserDao.class, ClientResponseHandler.class);
			return (Action) constructor.newInstance(userDao, clientResponseHandler);
		} catch (Exception e) {
			logger.error("Failed to create a UserAction instance, cause:", e);
			return null;
		}
	}
	
	private Action newInstance(String action, UserService userService) {
		try {
			String qualifiedClassName = ActionPathUtil.getQualifiedClassName(PathConstants.USER, action);
			Constructor<?> constructor = Class.forName(qualifiedClassName).getConstructor(UserService.class);
			return (Action) constructor.newInstance(userService);
		} catch (Exception e) {
			logger.error("Failed to create a UserAction instance, cause:", e);
			return null;
		}
	}
}
