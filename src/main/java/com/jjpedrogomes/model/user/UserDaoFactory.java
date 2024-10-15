package com.jjpedrogomes.model.user;

import javax.persistence.EntityManager;

import com.jjpedrogomes.model.util.JpaUtil;
import com.jjpedrogomes.repository.user.UserDaoImpl;

public class UserDaoFactory {
	
	public static UserDao<User> getInstance() {
		try {
			EntityManager entityManager = JpaUtil.getEntityManager();
			return new UserDaoImpl(entityManager);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
