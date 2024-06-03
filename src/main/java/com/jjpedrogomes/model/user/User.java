package com.jjpedrogomes.model.user;

import java.time.LocalDate;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.jjpedrogomes.model.shared.Entity;

@javax.persistence.Entity
@Table(name = "USER")
public class User implements Entity<User> {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
	private Long id;
	@Column(nullable = false)
	private String name;
	@Embedded
    @AttributeOverride(name = "address", column = @Column(name = "email", nullable = false))
	private Email email;
	@Embedded
    @AttributeOverride(name = "content", column = @Column(name = "password", nullable = false))
	private Password password;
	@Column(name = "birth_date", columnDefinition = "DATE", nullable = false)
	private LocalDate birthDate;
	@Transient
	private static final String REGEX_NAME = "[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+"; 
	
	public User (String name, Email email, Password password, LocalDate birthDate) {
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
	
	public Long getId() {
		return id;
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
