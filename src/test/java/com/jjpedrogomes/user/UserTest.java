package com.jjpedrogomes.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.jjpedrogomes.model.user.Email;
import com.jjpedrogomes.model.user.Password;
import com.jjpedrogomes.model.user.User;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserTest {
	
	private static User user;
	
	static {
		String name = "Cleiton Rasta";
		String address = "email@email.com";
		String password = "a1b2c3d4";
		LocalDate birthDate = LocalDate.of(1974, 9, 27);
		
		user = new User(name, new Email(address), new Password(password), birthDate);
	}
	
	@Test
	void create_user_with_all_params() {
		String name = "joe rogan";
		String address = "email@email.com";
		String password = "a1b2c3d4";
		LocalDate birthDate = LocalDate.of(1974, 9, 27);
		
		assertThatCode(() -> {
			User user = new User(name, new Email(address), new Password(password), birthDate);
			assertThat(user.getName()).isEqualTo("Joe Rogan");
		}).doesNotThrowAnyException();		
	}
	
	@Test
	void create_user_with_invalid_name() {
		String name = "joe 123";
		String address = "email@email.com";
		String password = "a1b2c3d4";
		LocalDate birthDate = LocalDate.of(1974, 9, 27);
		
		assertThatThrownBy(() -> {
			new User(name, new Email(address), new Password(password), birthDate);
		}).isExactlyInstanceOf(RuntimeException.class).hasMessage("Name can not be null or contain numbers");
	}
	
	@Test
	void create_user_with_null_birthdate() {
		String name = "joão pedro";
		String address = "email@email.com";
		String password = "a1b2c3d4";
		
		assertThatThrownBy(() -> {
			new User(name, new Email(address), new Password(password), null);
		}).isExactlyInstanceOf(RuntimeException.class).hasMessage("Birth date can not be null");
	}
	
	@Test
	void set_new_valid_name() {
		user.setName("João Pedro");
		assertThatCode(() -> {
			assertThat(user.getName()).isEqualTo("João Pedro");
		}).doesNotThrowAnyException();	
	}
	
	@Test
	void set_new_invalid_name() {
		assertThatThrownBy(() -> {
			user.setName("João Pedro123");
		}).isExactlyInstanceOf(RuntimeException.class).hasMessage("Name can not be null or contain numbers");
		user.setName("João Pedro");
	}
	
	
	@Nested
	class EmailTest {
		
		/**
         * Test to verify creating an Email object with a valid address string.
         * Ensures no exception is thrown and the email content matches.
         */
		@Test
		void create_matching_regex() {
			String address = "email@email.com";
			assertThatCode(() -> {
				Email email = new Email(address); 
				assertThat(email.getAddress()).isEqualTo(address);
			}).doesNotThrowAnyException();
		}
		
		/**
         * Test to verify creating an Email object with an invalid address string.
         * Ensures a RuntimeException is thrown with the appropriate message.
         */
		@Test
		void create_not_matching_regex() {
			String address = "email@";
			assertThatThrownBy(() -> {
				new Email(address);
			}).isExactlyInstanceOf(RuntimeException.class).hasMessage("Email is not valid");
		}
	}
	
	@Nested
	class PasswordTest {
		
		/**
         * Test to verify creating a Password object with a valid password string.
         * Ensures no exception is thrown and the password content matches.
         */
		@Test
		void create_matching_regex() {
			String content = "a1b2c3d4";
			assertThatCode(() -> {
				Password password = new Password(content); 
				assertThat(password.getContent()).isEqualTo(content);
			}).doesNotThrowAnyException();
		}
		
		/**
         * Test to verify creating a Password object with an invalid password string.
         * Ensures a RuntimeException is thrown with the appropriate message.
         */
		@Test
		void create_not_matching_regex() {
			String content = "invalid";
			assertThatThrownBy(() -> {
				new Password(content);
			}).isExactlyInstanceOf(RuntimeException.class).hasMessage("Password does not match requirements");
		}
		
		/**
         * Test to verify creating a Password object with a null password.
         * Ensures a RuntimeException is thrown with the appropriate message.
         */
		@Test
		void create_with_null_password() {
			assertThatThrownBy(() -> {
				new Password(null);
			}).isExactlyInstanceOf(RuntimeException.class).hasMessage("Password does not match requirements");
		}
		
		/**
         * Test to verify setting a new password different from the current one.
         * Ensures no exception is thrown and the password content is updated.
         */
		@Test
		void set_new_password() {
			String firstPassword = "a1b2c3d4";
			String secondPassword = "a1b4c3d6";
			Password password = new Password(firstPassword);
			assertThatCode(() -> {
				password.setNewPassword(new Password(secondPassword));
				assertThat(password.getContent()).isEqualTo(secondPassword);
			}).doesNotThrowAnyException();
		}
		
		/**
         * Test to verify setting a new password that is the same as the current one.
         * Ensures a RuntimeException is thrown with the appropriate message.
         */
		@Test
		void set_new_password_passing_same_password_as_active()  {
			String content = "a1b2c3d4";
			Password password = new Password(content);
			assertThatThrownBy(() -> {
				password.setNewPassword(password);
			}).isExactlyInstanceOf(RuntimeException.class).hasMessage("The given password is already being used");
		}	
	}
}
