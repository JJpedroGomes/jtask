package com.jjpedrogomes.controller.action;

import com.jjpedrogomes.controller.util.GsonUtil;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.repository.task.TaskDaoImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class UpdateTaskAction implements Action {

    private final TaskDaoImpl taskDao;
    private static final Logger logger = LogManager.getLogger(UpdateTaskAction.class);

    public UpdateTaskAction(TaskDaoImpl taskDao) {
        this.taskDao = taskDao;
    }

    /**
     * Updates an exiting task attributes
     *
     * @param request The HttpServletRequest containing task information.
     * @param response The HttpServletResponse for providing feedback.
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String idParam = request.getParameter("id");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String dueDate = request.getParameter("dueDate");

        Long id = null;
        try {
            id = Long.parseLong(idParam);
        } catch (NumberFormatException exception) {
            logger.error("Id must be a valid number", exception);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw exception;
        }

        Optional<Task> optionalTask = taskDao.get(id);
        optionalTask.ifPresent( task -> {
            updateTaskTitle(task, title);
            updateTaskDescription(task, description);
            updateTaskDueDate(task, dueDate);
            logger.info("Updating task...");
            taskDao.update(task);

            GsonUtil.convertObjectToJson(response, task);
        });
    }

    private void updateTaskDueDate(Task task, String dueDateParam) {
        if (dueDateParam != null) {
            try {
                LocalDate dueDate = LocalDate.parse(dueDateParam);
                task.setDueDate(dueDate);
            } catch (DateTimeParseException exception) {
                logger.error("Due data is not valid format", exception);
                throw exception;
            }
        }
    }

    private void updateTaskDescription(Task task, String description) {
        if (description != null) {
            task.setDescription(description);
        }
    }

    private void updateTaskTitle(Task task, String title) {
        if (title !=  null) {
            task.setTitle(title);
        }
    }
}
