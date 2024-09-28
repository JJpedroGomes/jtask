package com.jjpedrogomes.controller.task;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.task.TaskDao;

public class SetInProgressTaskAction implements Action {

    private final TaskDao<Task> taskDao;

    public SetInProgressTaskAction(TaskDao  taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String idParam = request.getParameter("id");
        try {
            Long id = Long.parseLong(idParam);
            taskDao.get(id).ifPresent(task -> {
    			task.setTaskInProgress();
    			taskDao.update(task);
    			response.setStatus(HttpServletResponse.SC_OK);
    		});
        } catch (NumberFormatException exception) {
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
