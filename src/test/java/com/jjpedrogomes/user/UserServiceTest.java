package com.jjpedrogomes.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.TestInstance;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jjpedrogomes.controller.auth.UserDto;
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
	
	class update_user {
		
		void update_user_with_all_params() {
			// Arrange
			String name = "newName";
			String birthDate = "1999-08-08";
			String password1 = "a1b2c3d4";
			String password2 = password1;
			UserDto userDto = new UserDto();
			userDto.setName(name)
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
		
		void update_user_with_few_params() {
			
		}
		
		void update_user_with_invalid_params() {
			
		}
		
		void update_password_but_second_password_is_not_same() {
			
		}	
	}
	
	private boolean checkIfPasswordMatches(String rawPassword, String encryptedPassword) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(rawPassword, encryptedPassword);
	}
}
