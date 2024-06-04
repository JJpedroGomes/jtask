package com.jjpedrogomes.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
	
	@BeforeEach
	 void setUpBeforeEach() {
		 this.entityManager = JpaUtil.getEntityManager();
		 this.userDao = new UserDao(entityManager);
	 }

	@Nested
	class user_dao_save {
		
		@Test
		void new_user() {
			String passwordContent = "a1b2c3d4";
			User user = new User("Javier Bardem", new Email("email@email.com"), new Password(passwordContent), LocalDate.of(1974, 9, 27));
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			// Act
			entityManager.getTransaction().begin();
			userDao.save(user);
			entityManager.getTransaction().commit();
			// Assert
			assertThat(user.getId()).isNotNull();
			assertTrue(passwordEncoder.matches(passwordContent, user.getPassword().getContent()));
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
	
	@Nested
	class user_dao_get {
		
		@Test
		void by_id_sucessfully() {
			// Arrange
			User user = new User("Ryan Gosling", new Email("email@email.com"), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
			entityManager.getTransaction().begin();
			userDao.save(user);
			entityManager.getTransaction().commit();
			// Act
			User userFromDb = userDao.get(user.getId()).get();
			// Assert
			assertThat(userFromDb).isEqualTo(user);
		}
		
		@Test
		void by_id_but_user_dont_exist() {
			// Arrange
			Long id = 100L;
			// Act
			Optional<User> optionalUser = userDao.get(id);
			// Assert
			assertThat(optionalUser).isEmpty();
		}
		
		@Test
		void all_users() {
			// Arrange
			User user1 = new User("Mike Faist", new Email("email@email.com"), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
			User user2 = new User("Zendaya", new Email("email@email.com"), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
			User user3 = new User("Josh Connor", new Email("email@email.com"), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
			
			List<User> userList = new ArrayList<User>();
			Collections.addAll(userList, user1, user2, user3);
			persistUsers(userList);
			// Act
			List<User> userListFromDb = userDao.getAll();
			// Assert
			assertThat(userListFromDb).containsAll(userList);			
		}

		private void persistUsers(List<User> userList) {
			entityManager.getTransaction().begin();
			userList.forEach(user -> userDao.save(user));
			entityManager.getTransaction().commit();;
		}
		
		@Test
		void all_user_empty() {
			 EntityManager entityManager = mock(EntityManager.class);
			 UserDao userDao = new UserDao(entityManager);
			 when(entityManager.createQuery(anyString(), eq(User.class)))
             					.thenThrow(new RuntimeException("Simulated Error"));
			 List<User> userListFromDb = userDao.getAll();
			 assertThat(userListFromDb).isEmpty();
		}
		
	}
}
