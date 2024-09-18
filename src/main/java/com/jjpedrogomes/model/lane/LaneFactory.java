package com.jjpedrogomes.model.lane;

import com.jjpedrogomes.model.user.User;

public class LaneFactory {
	
	public static Lane createLane(String name, User user) {
		Lane lane = new Lane(name, user);
		
		int lastPosition = user.getLanes().last().getPosition() + 1;
		
		lane.setPosition(lastPosition);
        user.setLaneToUser(lane);
        return lane;
	}
}
