package com.jjpedrogomes.controller.task;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.model.lane.LaneDao;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.task.TaskDao;

public class ChangeTaskPositionAction implements Action {
	
	private TaskDao<Task> taskDao;
	private LaneDao laneDao;

	public ChangeTaskPositionAction(TaskDao taskDao, LaneDao laneDao) {
		this.taskDao = taskDao;
		this.laneDao = laneDao;
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		String taskIdParam = request.getParameter("taskId");
		String laneIdParam = request.getParameter("laneId");
		String desiredIndexParam = request.getParameter("newPositionIndex");
		
		try {
			Long taskId = Long.valueOf(taskIdParam);
			Long laneId = Long.valueOf(laneIdParam);
			Integer desiredIndex = Integer.valueOf(desiredIndexParam);
			
			taskDao.get(taskId).ifPresent(task -> {
				laneDao.get(laneId).ifPresent(lane -> {
					if(task.getLane().equals(lane)) {
						lane.switchTaskPositionInsideLane(desiredIndex, task);
					} else {
						lane.addTaskIntoLanesPosition(desiredIndex, task);
					}
					laneDao.update(lane);
				});
			});
			
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
		}
	}
	
}
