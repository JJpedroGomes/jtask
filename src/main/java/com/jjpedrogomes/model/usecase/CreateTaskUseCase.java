package com.jjpedrogomes.model.usecase;

import com.jjpedrogomes.model.shared.UseCase;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.repository.TaskDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public class CreateTaskUseCase implements UseCase {

    private static final Logger logger = LogManager.getLogger(CreateTaskUseCase.class);
    private final TaskDao taskDao;

    public CreateTaskUseCase(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        LocalDate dueDate = LocalDate.parse(request.getParameter("dueDate"));
        Task task = new Task(title, description, dueDate);
        logger.info("Creating task...");
        taskDao.save(task);
    }
}
