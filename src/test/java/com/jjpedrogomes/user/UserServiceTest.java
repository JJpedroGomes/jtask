package com.jjpedrogomes.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jjpedrogomes.controller.auth.UserDto;
import com.jjpedrogomes.model.user.Email;
import com.jjpedrogomes.model.user.Password;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.user.UserService;
import com.jjpedrogomes.model.user.UserServiceFactory;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {
	
	private UserService userService;
	
	@BeforeEach
	void setUpBeforeEach() {
		this.userService = UserServiceFactory.getInstance();
	}
	
	@Nested
    @TestMethodOrder(OrderAnnotation.class)
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class update_user {
		
		private String email;
		private String password;
		
		@BeforeAll
		void setUpBeforaAll() {
			this.email = "random@email.com";
			this.password = "abcd@12345";
			User user = new User("Random Name", new Email(email), new Password(password), LocalDate.of(1974, 9, 27));
			UserDaoTest.persistUser(user);
		}
		
		@Test
		@Order(2)
		void update_user_with_all_params() {
			// Arrange
			String name = "Allparam";
			String birthDate = "1999-08-08";
			String password1 = "a1b2c3d4";
			String password2 = password1;
			UserDto userDto = new UserDto();
			userDto.setName(name)
				.setEmail(email)
				.setBirthDate(birthDate)
				.setPassword1(password1)
				.setPassword2(password2);
			// Act
			User user = userService.updateUser(userDto);
			// Assert
			assertThat(user.getName()).isEqualTo(name);
			assertThat(user.getBirthDate().toString()).isEqualTo(birthDate);
			assertTrue(checkIfPasswordMatches(password1, user.getPassword().getContent()));
		}
		
		@Test
		@Order(1)
		void update_user_with_few_params() {
			// Arrange
			String name = "Fewparam";
			String birthDate = "1999-09-09";
			UserDto userDto = new UserDto();
			userDto.setName(name)
				.setBirthDate(birthDate)
				.setEmail(email);
			// Act
			User user = userService.updateUser(userDto);
			// Assert
			assertThat(user.getName()).isEqualTo(name);
			assertThat(user.getBirthDate().toString()).isEqualTo(birthDate);
			assertTrue(checkIfPasswordMatches(password, user.getPassword().getContent()));
		}
		
		@Test
		@Order(3)
		void update_user_with_invalid_params() {
			// Arrange
			String name = "invalid Name 123";
			UserDto userDto = new UserDto();
			userDto.setName(name).setEmail(email);
			// Act & Assert
			assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userDto));
		}
		
		@Test
		@Order(4)
		void update_password_but_second_password_is_not_same() {
			// Arrange
			String password1 = "a1b2c3d4";
			String password2 = password1.toUpperCase();
			UserDto userDto = new UserDto();
			userDto.setPassword1(password1)
				.setPassword2(password2)
				.setEmail(email);
			// Act & Assert
			assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userDto));
		}	
		
		
		@Test
		@Order(5)
		void update_but_cannot_find_user() {
			// Arrange
			String email = "nonexistent@email.com";
			UserDto userDto = new UserDto();
			userDto.setEmail(email);
			// Act & Assert
			assertThrows(NoSuchElementException.class, () -> userService.updateUser(userDto));
		}
	}
	
	private boolean checkIfPasswordMatches(String rawPassword, String encryptedPassword) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(rawPassword, encryptedPassword);
	}
}
