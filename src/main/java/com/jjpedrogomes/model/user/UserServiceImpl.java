package com.jjpedrogomes.model.user;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jjpedrogomes.model.shared.ModelException;

public class UserServiceImpl implements UserService{
	
	private final UserDao<User> userDao;
	private final PasswordEncoder passwordEncoder;
	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	
	public UserServiceImpl(UserDao<User> userDao) {
		this.userDao = userDao;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}   

	/*
	 * Update the user attributes with the specified {@UserUpdateRequest} data.
	 * 
	 * @param abstraction of data needed to update user {@UserUpdateRequest}
	 * @return an {@User} after merging into the database with new data
	 * @throws IllegalArgumentException if {@UserUpdateRquest} are invalid
	 * @throws NoSuchElementException if there is no user with the given email in the database
	 */
	@Override
	public User updateUser(UserUpdateRequest userUpdateRequest) {
		logger.info("Entering method updateUser() in UserServiceImpl");
		
		try {
			User userFromDb = userDao.getUserByEmail(userUpdateRequest.getEmail()).get();
			if (userFromDb != null) {
				if (isPasswordInRequest(userUpdateRequest.getPassword1(), userUpdateRequest.getPassword2()) 
						&& isNotSameAsCurrent(userUpdateRequest.getPassword1(), userFromDb.getPassword().getContent())) {
					String encryptUserPassword = encryptUserPassword(userUpdateRequest.getPassword1());
					Password newPassword = new Password(encryptUserPassword);
					userFromDb.setPassword(newPassword);
				}
				
				if (userUpdateRequest.getName() != null) {
					userFromDb.setName(userUpdateRequest.getName());
				}
				
				if (userUpdateRequest.getBirthDate() != null) {
					LocalDate birthDate = LocalDate.parse(userUpdateRequest.getBirthDate());
					userFromDb.setBirthDate(birthDate);
				}		
			} 
			return userDao.update(userFromDb);
		} catch (ModelException e) {
			String logMessage = e.getErrorCode().getLogMessage();
			logger.error("Error Updating user: {}", logMessage, e);
			throw new IllegalArgumentException(logMessage, e);
		} 
	}
	
	private boolean isPasswordInRequest(String password1, String password2) {
		if(password1 == null) {
			return false;
		} else if(!password1.equals(password2)) {
			throw new InvalidPasswordException();
		}
		return true;
	}
	
	private boolean isNotSameAsCurrent(String passwordInRequest, String passwordFromDb) {
		return !passwordEncoder.matches(passwordInRequest, passwordFromDb);
	}
	
	private String encryptUserPassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

}
