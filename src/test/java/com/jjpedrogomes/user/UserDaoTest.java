package com.jjpedrogomes.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.jjpedrogomes.model.user.Email;
import com.jjpedrogomes.model.user.Password;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.util.JpaUtil;
import com.jjpedrogomes.repository.user.UserDao;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest {
	
	private EntityManager entityManager;
	private UserDao userDao;
	private User user;
	
	@BeforeAll
	void setUpBeforeAll() {
		this.user = new User("Javier Bardem", new Email("email@email.com"), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
	}
	
	@BeforeEach
	 void setUpBeforeEach() {
		 this.entityManager = JpaUtil.getEntityManager();
		 this.userDao = new UserDao(entityManager);
	 }

	@Nested
	class user_dao_save {
		
		@Test
		void new_user() {
			// Act
			entityManager.getTransaction().begin();
			userDao.save(user);
			entityManager.getTransaction().commit();
			// Assert
			assertThat(user.getId()).isNotNull();
		}
		
		@Test
		void new_user_with_error() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
			// Arrange
			User user = new User("Liam Neeson", new Email("email@email.com"), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
			Field name = User.class.getDeclaredField("name");
			name.setAccessible(true);
			name.set(user, null);
			// Act & Assert
			entityManager.getTransaction().begin();
			assertThrows(Exception.class, () -> userDao.save(user));
			entityManager.getTransaction().commit();	
		}
	}
}
