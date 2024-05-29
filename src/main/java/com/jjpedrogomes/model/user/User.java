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
	
	public Email getEmail() {
		return email;
	}
	
	public void setPassword(Password password) {
		this.password.setNewPassword(password);
	}
	
	public Password getPassword() {
		return password;
	}
	
	public boolean isAbleToRecoverPassword(Email email, LocalDate birthDate) {
		return this.email.equals(email) && this.birthDate.equals(birthDate);
	}
	
	@Override
	public boolean sameIdentityAs(User other) {
		return other != null && id.equals(other.id);
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
		return id.hashCode();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + "]";
	}
	
	public User() {}
}
