package com.jjpedrogomes.model.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserServiceImpl implements UserService{
	
	private final UserDao userDao;
	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	
	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}   

	@Override
	public User updateUser(UserUpdateRequest userUpdateRequest) {
		logger.info("Entering method updateUser() in UserServiceImpl");
		return null;
	}

}
