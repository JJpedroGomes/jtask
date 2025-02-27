package com.jjpedrogomes.model.lane;

import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.user.UserDao;
import com.jjpedrogomes.model.util.JpaUtil;
import com.jjpedrogomes.repository.lane.LaneDaoImpl;

public class LaneServiceFactory {

	public static LaneService getInstance() {
		return new LaneServiceImpl(new LaneDaoImpl(JpaUtil.getEntityManager()));
	}
	
	public static LaneService getInstance(UserDao<User> userDao) {
		return new LaneServiceImpl(new LaneDaoImpl(JpaUtil.getEntityManager()), userDao);
	}
}
