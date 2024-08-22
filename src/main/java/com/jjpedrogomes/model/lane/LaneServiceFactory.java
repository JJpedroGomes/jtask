package com.jjpedrogomes.model.lane;

import com.jjpedrogomes.model.util.JpaUtil;
import com.jjpedrogomes.repository.lane.LaneDaoImpl;

public class LaneServiceFactory {

	public static LaneService getInstance() {
		return new LaneServiceImpl(new LaneDaoImpl(JpaUtil.getEntityManager()));
	}
}
