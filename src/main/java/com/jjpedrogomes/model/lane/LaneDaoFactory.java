package com.jjpedrogomes.model.lane;

import javax.persistence.EntityManager;

import com.jjpedrogomes.model.util.JpaUtil;
import com.jjpedrogomes.repository.lane.LaneDaoImpl;

public class LaneDaoFactory {
	
	public static LaneDao getInstance() {
		try {
			EntityManager entityManager = JpaUtil.getEntityManager();
			return new LaneDaoImpl(entityManager);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
