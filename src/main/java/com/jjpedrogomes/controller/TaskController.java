package com.jjpedrogomes.controller;

import com.jjpedrogomes.model.usecase.*;
import com.jjpedrogomes.repository.JpaUtil;
import com.jjpedrogomes.repository.TaskDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "TaskController",
        urlPatterns = "/task"
)
public class TaskController extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(TaskController.class);
    private TaskDao taskDao;

    @Override
    public void init() throws ServletException {
        this.taskDao = new TaskDao(JpaUtil.getEntityManager());
    }

    //Todo - implement use cases logics
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("Entering method doPost() in TaskController Servlet");
        String action = request.getParameter("action");

        if (action.equals("CreateTask")) {
            CreateTaskUseCase useCase = new CreateTaskUseCase(taskDao);
            useCase.execute(request,response);
        } else if (action.equals("UpdateTask")) {
            UpdateTaskUseCase useCase = new UpdateTaskUseCase();
            useCase.execute(request,response);
        } else if (action.equals("DeleteTask")) {
            DeleteTaskUseCase useCase = new DeleteTaskUseCase();
            useCase.execute(request,response);
        } else if (action.equals("ConcludeTask")) {
            ConcludeTaskUseCase useCase = new ConcludeTaskUseCase();
            useCase.execute(request,response);
        } else if (action.equals("setInProgressTaskUseCase")) {
            SetInProgressTaskUseCase useCase = new SetInProgressTaskUseCase();
            useCase.execute(request,response);
        }

        //Todo - implement Dispatcher strategy
    }

    //Todo - implement getMethods with filters
}
