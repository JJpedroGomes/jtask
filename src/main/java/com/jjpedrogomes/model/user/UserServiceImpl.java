package com.jjpedrogomes.model.user;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jjpedrogomes.model.shared.ModelError;
import com.jjpedrogomes.model.shared.ModelException;

public class UserServiceImpl implements UserService{
	
	private final UserDao<User> userDao;
	private final PasswordEncoder passwordEncoder;
	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	
	public UserServiceImpl(UserDao<User> userDao) {
		this.userDao = userDao;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}   
	
	/**
	 * Save user in the database
	 * 
	 * @param name the name of the user
	 * @param emailArg the email of the user
	 * @param passwordArg the password of the user
	 * @param birthDate the birth date of the user
	 * @return a {@link User} after saving into the database
	 * @throws IllegalArgumentException if the arguments are invalid
	 * @throws PersistenceException if there is a problem saving the user
	 */
	@Override
	public User createUser(String name, String emailArg, String passwordArg, LocalDate birthDate) throws PersistenceException {
		if(isEmailAlreadyTaken(emailArg)) {
			throw new IllegalArgumentException(ModelError.EMAIL_ALREADY_TAKEN.getLogMessage());
		}
		
		try {
			Email email = new Email(emailArg);
			Password password = new Password(passwordArg);
			User user = new User(name, email, password, birthDate);
			
			userDao.save(user);
			return user;
		} catch (ModelException e) {
			String logMessage = e.getErrorCode().getLogMessage();
			logger.error("Error Updating user: {}", logMessage, e);
			throw new IllegalArgumentException(logMessage, e);
		}		
	}
	
	private boolean isEmailAlreadyTaken(String email) {
		return userDao.getUserByEmail(email).isPresent();
	}

	/**
	 * Update the user attributes with the specified {@link UserUpdateRequest} data.
	 * 
	 * @param userUpdateRequest an abstraction of data needed to update the user
	 * @return a {@link User} after merging the new data into the database
	 * @throws IllegalArgumentException if the {@link UserUpdateRequest} contains invalid data
	 * @throws NoSuchElementException if there is no user with the given email in the database
	 */
	@Override
	public User updateUser(UserUpdateRequest userUpdateRequest) throws NoSuchElementException {
		logger.info("Entering method updateUser() in UserServiceImpl");
		
		try {
			User userFromDb = userDao.getUserByEmail(userUpdateRequest.getEmail()).get();
			if (userFromDb != null) {
				if (isPasswordInRequest(userUpdateRequest.getPassword1(), userUpdateRequest.getPassword2()) 
						&& isNotSameAsCurrent(userUpdateRequest.getPassword1(), userFromDb.getPassword().getContent())) {
					Password password = new Password(userUpdateRequest.getPassword1());
					
					String encryptUserPassword = encryptUserPassword(password.getContent());
					userFromDb.setPassword(new Password(encryptUserPassword));
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
		if(password1 == null || password1.isEmpty()) {
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
