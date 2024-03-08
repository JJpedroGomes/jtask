package com.jjpedrogomes.controller.main;

import com.jjpedrogomes.controller.shared.Action;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.util.JpaUtil;
import com.jjpedrogomes.model.task.TaskDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@WebServlet(
        name = "TaskController",
        urlPatterns = "/main"
)
public class TaskController extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(TaskController.class);
    private TaskDao taskDao;

    @Override
    public void init() throws ServletException {
        logger.info("Servlet init");
        try {
            this.taskDao = new TaskDao(JpaUtil.getEntityManager());
        } catch (Exception exception) {
            logger.error("Error occurred trying to init the servlet");
            throw new ServletException(exception);
        }
    }

    //Todo - implement use cases logics
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("Entering method doPost() in TaskController Servlet");
        String action = request.getParameter("action");

        if (action == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("No action provided");
            return;
        }

        StringBuilder builder = new StringBuilder();
        String ACTION_PATH = "com.jjpedrogomes.controller.action.";
        String className = builder.append(ACTION_PATH).append(action).append("Action").toString();
        try {
            Constructor<?> constructor = Class.forName(className).getConstructor(TaskDao.class);
            Action actionClass = (Action) constructor.newInstance(taskDao);
            actionClass.execute(request, response);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("Action not found", e);
            throw new ServletException(e);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/board.jsp");
        dispatcher.forward(request, response);
    }

    //Todo - implement getMethods with filters
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entering method doGet() in TaskController Servlet");
        String action = request.getParameter("action");
        logger.info("action provided:" + action);

        if (action == null) {
            action = "LIST";
            logger.info("No action provided");
        }

        switch (action) {
            case "LIST":
                List<Task> taskList = taskDao.getAll();
                request.setAttribute("taskList", taskList);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/board.jsp");
                dispatcher.forward(request,response);
                logger.info("Returning all tasks");
                break;
        }
    }
}
