package com.jjpedrogomes.controller.task;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.controller.util.ActionPathUtil;
import com.jjpedrogomes.controller.util.PathConstants;
import com.jjpedrogomes.model.lane.LaneDao;
import com.jjpedrogomes.model.lane.LaneDaoFactory;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.task.TaskDao;
import com.jjpedrogomes.model.task.TaskDaoFactory;
import com.jjpedrogomes.repository.task.TaskDaoImpl;

@WebServlet(
        name = "TaskController",
        urlPatterns = "/main"
)
public class TaskController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final String ACTION_PATH = "com.jjpedrogomes.controller.task.";
	private static final Logger logger = LogManager.getLogger(TaskController.class);

//    @Override
//    public void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException {
//        logger.info("Entering method doPost() in TaskController Servlet");
//
//        EntityManager entityManager = (EntityManager) request.getAttribute("entityManager");
//        TaskDao<Task> taskDao = new TaskDaoImpl(entityManager);
//        String action = request.getParameter("action");
//
//        String qualifiedClassName = getQualifiedClassName(action, response);
//        try {
//            Constructor<?> constructor = Class.forName(qualifiedClassName).getConstructor(TaskDaoImpl.class);
//            Action actionClass = (Action) constructor.newInstance(taskDao);
//            actionClass.execute(request, response);
//        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException
//                 | InvocationTargetException e) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            logger.error("Action:" + action + "not found", e);
//            throw new ServletException(e);
//        }
//    }
	
	@Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        logger.info("Entering method doPost() in TaskController Servlet");

        EntityManager entityManager = (EntityManager) request.getAttribute("entityManager");
        String actionParam = request.getParameter("action");
        
        TaskDao taskDao = TaskDaoFactory.getInstance(entityManager);
        LaneDao laneDao = LaneDaoFactory.getInstance();
        Action action = newInstance(actionParam, taskDao, laneDao);
        
        if (action == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			throw new ServletException();
		}

        action.execute(request, response);
    }

    //Todo - implement getMethods with filters
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entering method doGet() in TaskController Servlet");

        EntityManager entityManager = (EntityManager) request.getAttribute("entityManager");
        TaskDaoImpl taskDao = new TaskDaoImpl(entityManager);
        String action = request.getParameter("action");
        logger.info("action provided:" + action);

        if (action == null) {
            action = "LIST";
            logger.info("No action provided");
        }

        switch (action) {
            case "LIST":
                List<Task> taskList = taskDao.getAll();

                HttpSession session = request.getSession(true);
                session.setAttribute("taskList", taskList);

                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/board.jsp");
                dispatcher.forward(request,response);
                logger.info("Returning all tasks");
                break;
        }
    }
    
	private Action newInstance(String actionParam, TaskDao taskDao, LaneDao laneDao) {	
		try {
			String qualifiedClassName = ActionPathUtil.getQualifiedClassName(PathConstants.TASK, actionParam);
			Class<?> clazz = Class.forName(qualifiedClassName);
			Constructor<?>[] constructors = clazz.getConstructors();
			
			for (Constructor<?> constructor : constructors) {
	            Class<?>[] parameterTypes = constructor.getParameterTypes();

	            // Check if the constructor matches the required parameters
	            if (parameterTypes.length == 2 
	                    && parameterTypes[0].equals(TaskDao.class) 
	                    && parameterTypes[1].equals(LaneDao.class)) {
	                return (Action) constructor.newInstance(taskDao, laneDao);
	            } else if (parameterTypes.length == 1 && parameterTypes[0].equals(TaskDao.class)) {
	                return (Action) constructor.newInstance(taskDao);
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
