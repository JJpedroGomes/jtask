package com.jjpedrogomes.model.user;

public class UserServiceFactory {
	
	public static UserService getInstance() {
		try {
			UserDao<User> userDao = UserDaoFactory.getInstance();
			return new UserServiceImpl(userDao);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
