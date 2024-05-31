package com.jjpedrogomes.model.user;

import java.time.LocalDate;

import com.jjpedrogomes.model.shared.Entity;

public class User implements Entity<User> {
	
	private Long id;
	private String name;
	private Email email;
	private Password password;
	private LocalDate birthDate;
	private static final String REGEX_NAME = "[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+"; 
	
	public User (String name, Email email, Password password, LocalDate birthDate) {
//		if (!isValidName(name)) {
//			throw new RuntimeException("Name can not be null or contain numbers");
//		} else if (birthDate == null) {
//			throw new RuntimeException("Birth date can not be null");
//		}
		validateName(name);
		if (birthDate == null) throw new RuntimeException("Birth date can not be null");
		this.name = capitalizeName(name);
		this.email = email;
		this.password = password;
		this.birthDate = birthDate;
	}
	
	private String capitalizeName(String name) {
		String[] words = name.split(" ");
        StringBuilder capitalized = new StringBuilder();
        
        for (String word : words) {
            if (word.length() > 0) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                           .append(word.substring(1).toLowerCase())
                           .append(" ");
            }
        }
        
        return capitalized.toString().trim();
	}
	
	public boolean isAbleToRecoverPassword(Email email, LocalDate birthDate) {
		return this.email.equals(email) && this.birthDate.equals(birthDate);
	}	
	
	public void setPassword(Password password) {
		this.password.setNewPassword(password);
	}
	
	public void setName(String name) {
		validateName(name);
		this.name = capitalizeName(name);
	}
	
	public Email getEmail() {
		return email;
	}
	
	public Password getPassword() {
		return password;
	}
	
	public String getName() {
		return name;
	}
	
	public LocalDate getBirthDate() {
		return birthDate;
	}
	
	private void validateName(String name) {
		if (name != null && name.matches(REGEX_NAME)) {
			return;
		}
		throw new RuntimeException("Name can not be null or contain numbers");
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
