package com.jjpedrogomes.controller.task;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.repository.task.TaskDaoImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@WebServlet(
        name = "TaskController",
        urlPatterns = "/main"
)
public class TaskController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final String ACTION_PATH = "com.jjpedrogomes.controller.task.";
	private static final Logger logger = LogManager.getLogger(TaskController.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        logger.info("Entering method doPost() in TaskController Servlet");

        EntityManager entityManager = (EntityManager) request.getAttribute("entityManager");
        TaskDao<Task> taskDao = new TaskDaoImpl(entityManager);
        String action = request.getParameter("action");

        String qualifiedClassName = getQualifiedClassName(action, response);
        try {
            Constructor<?> constructor = Class.forName(qualifiedClassName).getConstructor(TaskDaoImpl.class);
            Action actionClass = (Action) constructor.newInstance(taskDao);
            actionClass.execute(request, response);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException
                 | InvocationTargetException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("Action:" + action + "not found", e);
            throw new ServletException(e);
        }
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

    private String getQualifiedClassName(String action, HttpServletResponse response) throws ServletException {
        if (action == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("No action provided");
            throw new ServletException();
        }
        StringBuilder builder = new StringBuilder();
        return builder.append(ACTION_PATH).append(action).append("Action").toString();
    }
}
