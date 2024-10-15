package com.jjpedrogomes.controller.lane;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.model.lane.LaneService;

public class UpdateLaneNameAction implements Action {
	
	private final LaneService laneService;
	
	public UpdateLaneNameAction(LaneService laneService) {
		this.laneService = laneService;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		String newNameParam = request.getParameter("newTitle");
		String laneIdParam = request.getParameter("laneId");
		String userEmail = (String) request.getSession().getAttribute("user");
			
		if (newNameParam == null || laneIdParam == null) {
			throw new IllegalArgumentException();
		}
			
		laneService.updateLaneName(Long.valueOf(laneIdParam), newNameParam, userEmail);
	}
}
