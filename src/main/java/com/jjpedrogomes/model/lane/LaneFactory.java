package com.jjpedrogomes.model.lane;

import com.jjpedrogomes.model.user.User;

public class LaneFactory {
	
	public static Lane createLane(String name, User user) {
		Lane lane = new Lane(name, user);
        user.getLanes().add(lane);
		lane.setPosition(user.getLanes().size());
        return lane;
	}
}
