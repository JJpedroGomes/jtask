package com.jjpedrogomes.controller.lane;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.model.lane.LaneService;

public class SwitchLanePositionAction implements Action {
	
	private final LaneService laneService;
	private static final Logger logger = LogManager.getLogger(SwitchLanePositionAction.class);
	
	public SwitchLanePositionAction(LaneService laneService) {
		this.laneService = laneService;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		logger.info("Entering SwitchLaneAction...");
		String laneIdParam = request.getParameter("laneId");
		String desiredIndexParam = request.getParameter("newPositionIndex");
		
		try {
			Long laneId = Long.valueOf(laneIdParam);
			Integer desiredIndex = Integer.valueOf(desiredIndexParam);
			
			laneService.switchLanePosition(laneId, desiredIndex);
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
		}
	}
}
