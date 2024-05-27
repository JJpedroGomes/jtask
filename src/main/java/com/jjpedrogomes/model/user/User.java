package com.jjpedrogomes.model.user;

import java.time.LocalDate;

import com.jjpedrogomes.model.shared.Entity;

public class User implements Entity<User> {
	
	private Long id;
	private String name;
	private Email email;
	private Password password;
	private LocalDate birthDate;
	
	public User (String name, Email email, Password password, LocalDate birthDate) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.birthDate = birthDate;
	}
	
	public void setPassword(Password password) {
		this.password = password;
	}
	
	@Override
	public boolean sameIdentityAs(User other) {
		return other != null && email.equals(other.email);
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		final User other = (User) object;
		return sameIdentityAs(other);
	}
	
	@Override
	public int hashCode() {
		return email.hashCode();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + "]";
	}
	
	public User() {
		// Needed By hibernate
	}
}
