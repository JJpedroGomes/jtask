package com.jjpedrogomes.model.user;

import com.jjpedrogomes.model.shared.ValueObject;

public class Email implements ValueObject<Email>{
	
	private String address;
	private static final String REGEX_EMAIL = "^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	
	public Email(String address) {
		if (!isEmailValid(address)) {
			throw new RuntimeException("Email is not valid");
		}
		this.address = address;
	}
	
	public String getAddress() {
		return address;
	}
	
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
