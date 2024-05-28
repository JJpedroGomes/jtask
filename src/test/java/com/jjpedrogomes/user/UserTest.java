package com.jjpedrogomes.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.jjpedrogomes.model.user.Password;
import com.jjpedrogomes.model.user.User;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserTest {
	
	static User buildUser() {
		return new User("Josh", null, null, null);
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
				assertThat(password.getPasswordContent()).isEqualTo(content);
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
				assertThat(password.getPasswordContent()).isEqualTo(secondPassword);
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
