package com.jjpedrogomes.repository.user;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jjpedrogomes.model.user.Password;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.repository.shared.Dao;

public class UserDao implements Dao<User>{
	
	private final EntityManager entityManager;
	private final PasswordEncoder passwordEncoder;
	private static final Logger logger = LogManager.getLogger(UserDao.class);
	
	public UserDao(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.passwordEncoder = new BCryptPasswordEncoder();
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
		String query = "SELECT u from User u";
        try {
            logger.info("Selecting all Tasks");
            List<User> list = this.entityManager.createQuery(query, User.class).getResultList();
            if (list.isEmpty()) logger.info("Task List is current empty");
            return list;
        } catch (Exception exception) {
            logger.error("Error while retrieving all tasks", exception);
            return Collections.emptyList();
        }
	}

	@Override
	public void save(User user) {
		String encryptedPassword = passwordEncoder.encode(user.getPassword().getContent());
		user.setPassword(new Password(encryptedPassword));
		entityManager.persist(user);
		logger.info("User saved successfully.");
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
