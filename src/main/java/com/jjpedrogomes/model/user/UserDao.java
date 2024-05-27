package com.jjpedrogomes.model.user;

import java.util.List;
import java.util.Optional;


import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jjpedrogomes.model.shared.Dao;

public class UserDao implements Dao<User>{
	
	private final EntityManager entityManager;
	private static final Logger logger = LogManager.getLogger(UserDao.class);
	
	public UserDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Optional<User> get(long id) {
		try {
			User user = this.entityManager.find(User.class, id);
			return Optional.ofNullable(user);
		} catch (Exception exception) {
			logger.error("Error while retrieving user with id: " + id, exception);
			return Optional.empty();
		}
	}

	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(User t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User update(User t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(User t) {
		// TODO Auto-generated method stub
		
	}

}
