package com.jjpedrogomes.controller.lane;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.model.lane.LaneService;
import com.jjpedrogomes.model.user.UserDao;

public class DeleteLaneAction implements Action{
	
	private final LaneService laneService;
	
	public DeleteLaneAction(LaneService laneService, UserDao userDao) {
		this.laneService = laneService;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		String laneIdParam = request.getParameter("laneId");
		if (laneIdParam == null) {
			throw new IllegalArgumentException();
		}
		
		laneService.deleteLane(Long.valueOf(laneIdParam));
	}
}
