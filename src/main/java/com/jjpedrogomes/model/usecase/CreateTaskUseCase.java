package com.jjpedrogomes.model.usecase;

import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.repository.TaskDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CreateTaskUseCase {

    private static final Logger logger = LogManager.getLogger(CreateTaskUseCase.class);
    private final TaskDao taskDao;

    public CreateTaskUseCase(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    /**
     * Executes the use case to create a new task.
     *
     * @param request  The HttpServletRequest containing task information.
     * @param response The HttpServletResponse for providing feedback.
     */
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String title = request.getParameter("title");
        validateTitle(title);
        String description = request.getParameter("description");
        String dueDateParam = request.getParameter("dueDate");

        LocalDate dueDate = null;
        if (dueDateParam != null) {
            dueDate = parseDueDate(dueDateParam);
        }
        Task task = new Task(title, description, dueDate);
        logger.info("Creating task...");
        taskDao.save(task);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    private void validateTitle(String title) {
        if (title == null) {
            IllegalArgumentException exception = new IllegalArgumentException();
            logger.error("Task title cannot be null", exception);
            throw exception;
        }
    }

    private LocalDate parseDueDate(String dueDateParam) {
        try {
            return LocalDate.parse(dueDateParam);
        } catch (DateTimeParseException exception) {
            logger.error("Due data is not valid format", exception);
            throw exception;
        }
    }
}
