package com.jjpedrogomes.controller.task;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.controller.util.GsonUtil;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.task.TaskDao;

public class DeleteTaskAction implements Action {

    private final TaskDao taskDao;
    private static final Logger logger = LogManager.getLogger(DeleteTaskAction.class);

    public DeleteTaskAction(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    /**
     * Delete an exiting task
     *
     * @param request The HttpServletRequest containing task information.
     * @param response The HttpServletResponse for providing feedback.
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Entering delete task action");
        String idParam = request.getParameter("id");
        Long id = null;
        try {
            id = Long.parseLong(idParam);
        } catch (NumberFormatException exception) {
            logger.error("Id must be a valid number", exception);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw exception;
        }
        taskDao.get(id).ifPresent(task -> {
        	taskDao.delete(task);
        	GsonUtil.convertObjectToJson(response, new TaskDto((Task) task));
        });
    }
}
