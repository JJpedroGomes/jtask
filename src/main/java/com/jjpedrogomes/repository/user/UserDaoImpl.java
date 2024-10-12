package com.jjpedrogomes.repository.user;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.user.UserDao;

public class UserDaoImpl implements UserDao<User>{
	
	private final EntityManager entityManager;
	private final PasswordEncoder passwordEncoder;
	private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);
	
	public UserDaoImpl(EntityManager entityManager) {
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
		logger.info("Entering method save in UserDaoImpl");
		EntityTransaction transaction = entityManager.getTransaction();
		if (!transaction.isActive()) {
			transaction.begin();
		}
		try {
			entityManager.persist(user);
			transaction.commit();
			logger.info("User saved successfully.");
		} catch (Exception e) {
			logger.error("Unexpected error saving user: {}", e.getMessage());
			transaction.rollback();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public User update(User user) {
		return entityManager.merge(user);
	}

	@Override
	public void delete(User user) {
		User managedUser = entityManager.merge(user);
		managedUser.inactivateUser();
	}

	public Optional<User> getUserByCredentials(String email, String password) {
		return getUserByEmail(email).filter(user -> passwordEncoder.matches(password, user.getPassword().getContent()));
	}

	@Override
	public Optional<User> getUserByEmail(String email) {
		try {
			String query = "SELECT u FROM User u WHERE u.email.address = :email";			
			return Optional.ofNullable(entityManager.createQuery(query, User.class)
				.setParameter("email", email)
				.getSingleResult());	
		} catch (Exception e) {
			return Optional.empty();
		}
	}
}
