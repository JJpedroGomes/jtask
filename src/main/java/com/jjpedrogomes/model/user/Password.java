package com.jjpedrogomes.model.user;

import javax.persistence.Embeddable;

import com.jjpedrogomes.model.shared.ValueObject;

/**
 * The Password class represents a user's password as a value object.
 * It enforces certain rules for password validity and provides methods to handle password operations.
 */
@Embeddable
public class Password implements ValueObject<Password>{
	
	private String content;
	
	// Regular expression to enforce password rules: at least one digit, one lower case letter, and minimum 8 characters
	private static final String REGEX_PASSWORD = "(?=.*[0-9])(?=.*[a-z]).{8,}";
	
	/**
     * Constructor for creating a Password object.
     * Validates the password content based on defined rules.
     *
     * @param content the actual password string
     * @throws RuntimeException if the password does not match the required pattern
     */
	public Password (String content) {
		if (!isPasswordValid(content)) {
			throw new RuntimeException("Password does not match requirements");
		}
		this.content = content;
	}
	
	/**
     * Sets a new password.
     * Ensures the new password is different from the current one.
     *
     * @param password the new Password object
     * @throws RuntimeException if the new password is the same as the current one
     */
	public void setNewPassword(Password password) {
		if (sameValueAs(password)) {
			throw new RuntimeException("The given password is already being used");
		}
		this.content = password.content;
	}
	
	/**
     * Validates the password content against the predefined pattern.
     *
     * @param content the password string to be validated
     * @return true if the password matches the pattern, false otherwise
     */
	private boolean isPasswordValid(String content) {
		return content != null && content.matches(REGEX_PASSWORD);
	}
	
	public String getContent() {
		return content;
	}
		
	@Override
	public boolean sameValueAs(Password other) {
		return other != null && content.equals(other.content);
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object)return true;
		if (object == null || getClass() != object.getClass()) return false;
		Password other = (Password) object;		
		return sameValueAs(other);
	}
	
	@Override
	public int hashCode() {
		return content.hashCode();
	}

	@Override
	public String toString() {
		return "Password [content=" + content + "]";
	}
	
	Password() {}
}
