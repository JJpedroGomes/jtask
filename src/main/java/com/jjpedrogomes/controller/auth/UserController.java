package com.jjpedrogomes.controller.auth;

import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.controller.util.ActionPathUtil;
import com.jjpedrogomes.controller.util.PathConstants;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.repository.user.UserDaoImpl;

@WebServlet(
        name = "TaskController",
        urlPatterns = "/user"
)
public class UserController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(UserController.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("Entering method doPost() in UserController Servlet");
		
		EntityManager entityManager = (EntityManager) request.getAttribute("entityManager");
		UserDao<User> userDao = new UserDaoImpl(entityManager);
		
		String action = request.getParameter("action");
		
		String qualifiedClassName = ActionPathUtil.getQualifiedClassName(PathConstants.USER, action, response);
		try {
			Constructor<?> constructor = Class.forName(qualifiedClassName).getConstructor(UserDaoImpl.class);
            Action actionClass = (Action) constructor.newInstance(userDao);
            actionClass.execute(request, response);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("Action:" + action + "not found", e);
            throw new ServletException(e);
		}
	}
}
