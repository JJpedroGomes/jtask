package com.jjpedrogomes.controller.task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.controller.util.GsonUtil;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.task.TaskDao;

public class UpdateTaskAction implements Action {

    private final TaskDao taskDao;
    private static final Logger logger = LogManager.getLogger(UpdateTaskAction.class);

    public UpdateTaskAction(TaskDao taskDao) {
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

            GsonUtil.convertObjectToJson(response, new TaskDto(task));
        });
    }

    private void updateTaskDueDate(Task task, String dueDateParam) {
    	LocalDate dueDate = null;
    	if (dueDateParam != null && !dueDateParam.isEmpty()) {
    		dueDate = LocalDate.parse(dueDateParam);
    	}
    	task.setDueDate(dueDate);
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
