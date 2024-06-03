package com.jjpedrogomes.model.user;

import javax.persistence.Embeddable;

import com.jjpedrogomes.model.shared.ValueObject;


/**
 * The Email class represents an email address as a value object.
 * It includes validation to ensure the email address is in a valid format.
 */
@Embeddable
public class Email implements ValueObject<Email>{
	
	private String address;
	private static final String REGEX_EMAIL = "^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	
	/**
     * Constructs an Email object with the given address.
     * Validates the email address format before assigning it.
     * 
     * @param address The email address to be stored.
     * @throws RuntimeException if the email address is not valid.
     */
	public Email(String address) {
		if (!isEmailValid(address)) {
			throw new RuntimeException("Email is not valid");
		}
		this.address = address;
	}
	
	public String getAddress() {
		return address;
	}
	
	  /**
     * Validates the given email address using a regular expression.
     * 
     * @param address The email address to validate.
     * @return true if the email address is valid, false otherwise.
     */	
	private boolean isEmailValid(String address) {
		return address != null && address.matches(REGEX_EMAIL);
	}

	@Override
	public boolean sameValueAs(Email other) {
		return other != null && address.matches(other.address);
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object)return true;
		if (object == null || getClass() != object.getClass()) return false;
		Email other = (Email) object;
		return sameValueAs(other);
	}

	@Override
	public int hashCode() {
		return address.hashCode();
	}

	@Override
	public String toString() {
		return "Email [address=" + address + "]";
	}	
	
	Email() {}
}
