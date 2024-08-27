package com.jjpedrogomes.model.lane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.user.UserDao;

public class LaneServiceImpl implements LaneService {
	
	private final LaneDao laneDao;
	private UserDao<User> userDao;
	private static final Logger logger = LogManager.getLogger(LaneServiceImpl.class);
	
	public LaneServiceImpl(LaneDao laneDao, UserDao<User> userDao) {
		this.laneDao = laneDao;
		this.userDao = userDao;
	}
	
	public LaneServiceImpl(LaneDao laneDao) {
		this.laneDao = laneDao;
	}

	@Override
	public Lane createLane(String name, User user) {
		logger.info("Creating user...");		
		Lane lane = LaneFactory.createLane(name, user);
		
		this.userDao.update(user);
		return lane;
	}

	@Override
	public Lane updateLaneName(Long id, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void switchLanePosition(Long id, int desiredIndex) {
		logger.info("Switching lanes position...");
		Lane lane = laneDao.get(id).orElseThrow(() -> new RuntimeException("Lane not found for id: " + id));
		lane.switchLanePositionForUser(desiredIndex);
		userDao.update(lane.getUser());
	}

	@Override
	public void removeTaskFromLane(Long id, Task Task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addNewTaskLastToLane(Long id, Task task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeTaskPositionInLanes(Long currentLaneId, Long desiredLaneId, Long taskId, int desiredIndex) {
		// TODO Auto-generated method stub
		
	}

}
