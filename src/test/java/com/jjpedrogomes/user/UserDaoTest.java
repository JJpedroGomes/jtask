package com.jjpedrogomes.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import com.jjpedrogomes.model.user.UserDao;
import com.jjpedrogomes.model.util.JpaUtil;
import com.jjpedrogomes.repository.user.UserDaoImpl;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest {
	
	private EntityManager entityManager;
	private UserDaoImpl userDao;
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@BeforeEach
	 void setUpBeforeEach() {
		 this.entityManager = JpaUtil.getEntityManager();
		 this.userDao = new UserDaoImpl(entityManager);
	 }

	@Nested
	class user_dao_save {
		
		@Test
		void new_user() {
			String passwordContent = "a1b2c3d4";
			User user = new User("Javier Bardem", new Email("email@email.com"), new Password(passwordContent), LocalDate.of(1974, 9, 27));
			// Act
			persistUser(user);
			// Assert
			assertThat(user.getId()).isNotNull();
			assertTrue(passwordEncoder.matches(passwordContent, user.getPassword().getContent()));
		}
		
		@Test
		void new_user_with_error() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
			// Arrange
			User user = new User("Liam Neeson", new Email("email1@email.com"), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
			Field name = User.class.getDeclaredField("name");
			name.setAccessible(true);
			name.set(user, null);
			// Act & Assert
			assertThrows(Exception.class, () -> userDao.save(user));	
		}
	}
	
	@Nested
	class user_dao_get {
		
		@Test
		void by_id_sucessfully() {
			// Arrange
			User user = new User("Ryan Gosling", new Email("email2@email.com"), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
			persistUser(user);
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
			User user1 = new User("Mike Faist", new Email("email3@email.com"), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
			User user2 = new User("Zendaya", new Email("email4@email.com"), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
			User user3 = new User("Josh Connor", new Email("email5@email.com"), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
			
			List<User> userList = new ArrayList<User>();
			Collections.addAll(userList, user1, user2, user3);
			persistUsers(userList);
			// Act
			List<User> userListFromDb = userDao.getAll();
			// Assert
			assertThat(userListFromDb).containsAll(userList);			
		}
		
		@Test
		void all_user_empty() {
			 EntityManager entityManager = mock(EntityManager.class);
			 UserDaoImpl userDao = new UserDaoImpl(entityManager);
			 when(entityManager.createQuery(anyString(), eq(User.class)))
             					.thenThrow(new RuntimeException("Simulated Error"));
			 List<User> userListFromDb = userDao.getAll();
			 assertThat(userListFromDb).isEmpty();
		}
		
		@Test
		void user_by_credentials() {
			// Arrange
			String email = "email11@email.com"; 
			String password = "a1b2c3d4";
			User user = new User("Harrison Ford", new Email(email), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
			persistUser(user);
			// Act
			Optional<User> userFromDb = userDao.getUserByCredentials(email, password);
			// Assert
			assertTrue(userFromDb.isPresent());
		}
		
		@Test
		void user_by_credentials_but_one_is_wrong() {
			// Arrange
			String email = "email10@email.com"; 
			String password = "a1b2c3d4";
			User user = new User("Harrison Ford", new Email(email), new Password("a1b2c3d8"), LocalDate.of(1974, 9, 27));
			persistUser(user);
			// Act
			Optional<User> userFromDb = userDao.getUserByCredentials(email, password);
			// Assert
			assertFalse(userFromDb.isPresent());
		}
		
		@Test
		void user_by_email() {
			// Arrange
			String email = "email12@email.com";
			User user = new User("Pedro Pascal", new Email(email), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
			persistUser(user);
			// Act
			Optional<User> userFromDb = userDao.getUserByEmail(email);
			// Assert
			assertTrue(userFromDb.isPresent());
		}	
	}
	
	@Nested
	class user_dao_delete {
		
		@Test
		void active_user() {
			// Arrange
			User user = new User("Joe Pesci", new Email("email6@email.com"), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
			persistUser(user);
			// Act
			entityManager.getTransaction().begin();
			userDao.delete(user);
			entityManager.getTransaction().commit();
			
			User userFromDb = userDao.get(user.getId()).get();
			// Assert
			assertThat(userFromDb.getIsActive()).isFalse();
		}
		
	}
	
	@Nested
	class user_dao_update {
		
		@Test
		void user_sucessfully() {
			// Arrange
			String passwordContent = "a1b2c3d4";
			User user = new User("Charlize Theron", new Email("email7@email.com"), new Password(passwordContent), LocalDate.of(1974, 9, 27));
			persistUser(user);
			// Act
			entityManager.getTransaction().begin();
			String newPasswordContent = "a123b123c123d123";
			String newName = "anya taylor-joy";
			
			user.setPassword(new Password(newPasswordContent));
			user.setName(newName);
			User userAfterUpdate = userDao.update(user);
			entityManager.getTransaction().commit();
			// Assert
			assertTrue(passwordEncoder.matches(newPasswordContent, userAfterUpdate.getPassword().getContent()));
			assertFalse(passwordEncoder.matches(passwordContent, userAfterUpdate.getPassword().getContent()));
			assertThat(userAfterUpdate.getName()).isEqualTo("Anya Taylor-Joy");
		}
	}
	
	private void persistUsers(List<User> userList) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		UserDao<User> userDao = new UserDaoImpl(entityManager);
		userList.forEach(user -> userDao.save(user));
	}
	
	public static void persistUser(User user) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		UserDao<User> userDao = new UserDaoImpl(entityManager);
		userDao.save(user);
	}
}
