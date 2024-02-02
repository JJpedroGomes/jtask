package com.jjpedrogomes.controller;

import com.jjpedrogomes.model.usecase.*;

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

    //Todo - implement use cases logics
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action.equals("CreateTask")) {
            CreateTaskUseCase useCase = new CreateTaskUseCase();
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
