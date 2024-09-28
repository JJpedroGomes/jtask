package com.jjpedrogomes.controller.task;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.task.TaskDao;

public class ConcludeTaskAction implements Action {
	
	private final TaskDao<Task> taskDao;
	
	public ConcludeTaskAction(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
    	String idParam = request.getParameter("id");
    	
    	Long id = Long.parseLong(idParam);
    	
    	try {    		
    		taskDao.get(id).ifPresent(task -> {
    			task.setTaskCompleted();
    			taskDao.update(task);
    			response.setStatus(HttpServletResponse.SC_OK);
    		});
    	} catch(Exception e) {
    		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    	}
    }
}
