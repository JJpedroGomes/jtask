package com.jjpedrogomes.controller;

import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.usecase.*;
import com.jjpedrogomes.repository.JpaUtil;
import com.jjpedrogomes.repository.TaskDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(
        name = "TaskController",
        urlPatterns = "/"
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

        switch (action) {
            case "CreateTask":
                logger.info("CreateTask action provided");
                CreateTaskUseCase createTaskUseCase = new CreateTaskUseCase(taskDao);
                createTaskUseCase.execute(request, response);
                break;
            case "UpdateTask":
                logger.info("UpdateTask action provided");
                UpdateTaskUseCase updateTaskUseCase = new UpdateTaskUseCase(taskDao);
                updateTaskUseCase.execute(request, response);
                break;
            case "DeleteTask":
                logger.info("DeleteTask action provided");
                DeleteTaskUseCase deleteTaskUseCase = new DeleteTaskUseCase();
                deleteTaskUseCase.execute(request, response);
                break;
            case "ConcludeTask":
                ConcludeTaskUseCase concludeTaskUseCase = new ConcludeTaskUseCase();
                concludeTaskUseCase.execute(request, response);
                break;
            case "setInProgressTaskUseCase":
                SetInProgressTaskUseCase setInProgressTaskUseCase = new SetInProgressTaskUseCase(taskDao);
                setInProgressTaskUseCase.execute(request, response);
                break;
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid action");
                break;
        }

        //Todo - implement Dispatcher strategy
    }

    //Todo - implement getMethods with filters

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entering method doGet() in TaskController Servlet");
        String action = request.getParameter("action");

        if (action == null) {
            action = "LIST";
            logger.info("No action provided");
        }

        switch (action) {
            case "LIST":
                List<Task> taskList = taskDao.getAll();
                request.setAttribute("taskList", taskList);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/index.jsp");
                dispatcher.forward(request,response);
                logger.info("Returning all tasks");
                break;
        }
    }
}
