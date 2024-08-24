package com.jjpedrogomes.controller.lane;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.model.lane.LaneService;

public class SwitchLanePositionAction implements Action {
	
	private final LaneService laneService;
	
	public SwitchLanePositionAction(LaneService laneService) {
		this.laneService = laneService;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		
	}
}
