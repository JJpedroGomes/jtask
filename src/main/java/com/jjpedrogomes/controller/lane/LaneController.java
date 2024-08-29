package com.jjpedrogomes.controller.lane;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
import com.jjpedrogomes.model.lane.LaneService;
import com.jjpedrogomes.model.lane.LaneServiceFactory;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.user.UserDao;
import com.jjpedrogomes.model.user.UserDaoFactory;

@WebServlet(
        name = "LaneController",
        urlPatterns = "/lane"
)
public class LaneController extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(LaneController.class);
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("Entering method doPost() in LaneController Servlet");
		
		String actionParam = request.getParameter("action");	
		Action action = newInstance(actionParam);
		
		if (action == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		action.execute(request, response);
	}

	private Action newInstance(String actionParam) {	
		try {
			String qualifiedClassName = ActionPathUtil.getQualifiedClassName(PathConstants.Lane, actionParam);
			Class<?> clazz = Class.forName(qualifiedClassName);
			Constructor<?>[] constructors = clazz.getConstructors();
			
			for (Constructor<?> constructor : constructors) {
	            Class<?>[] parameterTypes = constructor.getParameterTypes();

	            // Check if the constructor matches the required parameters
	            if (parameterTypes.length == 2 
	                    && parameterTypes[0].equals(LaneService.class) 
	                    && parameterTypes[1].equals(UserDao.class)) {
	            	UserDao<User> instance = UserDaoFactory.getInstance();
	                return (Action) constructor.newInstance(LaneServiceFactory.getInstance(instance), instance);
	            } else if (parameterTypes.length == 1 && parameterTypes[0].equals(LaneService.class)) {
	                return (Action) constructor.newInstance(LaneServiceFactory.getInstance());
	            }
	        }
			
			throw new NoSuchMethodException("No suitable constructor found for class: " + qualifiedClassName);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			logger.error("method or constructor was not found");
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.error("class was not located");
			return null;
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			logger.error("class object cannot be instantiated.");
			return null;
		} catch (RuntimeException e) {
			e.printStackTrace();
			logger.error("Action cannot be null");
			return null;
		}
	}
}
