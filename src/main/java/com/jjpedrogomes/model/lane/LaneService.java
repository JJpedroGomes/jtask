package com.jjpedrogomes.model.lane;

import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.user.User;

public interface LaneService {
	
	Lane createLane(String name, User user);
	
	Lane updateLaneName(Long id, String name);
	
	void switchLanePosition(Long id, int desiredIndex);
	
	void removeTaskFromLane(Long id, Task Task);
	
	void addNewTaskLastToLane(Long id, Task task);
	
	void changeTaskPositionInLanes(Long currentLaneId, Long desiredLaneId, Long taskId, int desiredIndex);
}
