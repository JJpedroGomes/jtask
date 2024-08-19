package com.jjpedrogomes.model.user;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.jjpedrogomes.model.lane.Lane;


@Entity
@Table(name = "users")
public class User implements com.jjpedrogomes.model.shared.Entity<User> {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
	private Long id;
	@Column(nullable = false)
	private String name;
	@Embedded
    @AttributeOverride(name = "address", column = @Column(name = "email", nullable = false, unique = true))
	private Email email;
	@Embedded
    @AttributeOverride(name = "content", column = @Column(name = "password", nullable = false))
	private Password password;
	@Column(name = "birth_date", columnDefinition = "DATE", nullable = false)
	private LocalDate birthDate;
	@Column(name = "creation_date", columnDefinition = "DATE", nullable = false)
	private LocalDate creationDate;
	@Column(name = "is_active" ,nullable = false)
	private boolean isActive;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Lane> lanes = new TreeSet<Lane>();
	@Transient
	private static final String REGEX_NAME = "[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+"; 
	
	public User (String name, Email email, Password password, LocalDate birthDate) {
		validateName(name);
		if (birthDate == null) throw new RuntimeException("Birth date can not be null");
		this.name = capitalizeName(name);
		this.email = email;
		this.password = password;
		this.birthDate = birthDate;
		this.creationDate = LocalDate.now();
		this.isActive = true;
	}
	
	private String capitalizeName(String name) {
		char[] charArray = name.toCharArray();
        StringBuilder capitalized = new StringBuilder();

        capitalized.append(Character.toUpperCase(charArray[0]));
        
        for (int i = 1; i < charArray.length; i++) {
        	if (charArray[i - 1] == ' ' || charArray[i - 1] == '-') {
        		capitalized.append(Character.toUpperCase(charArray[i]));
        		continue;
        	}
        	capitalized.append(charArray[i]);
        }        
        
        return capitalized.toString().trim();
	}
	
	public boolean isAbleToRecoverPassword(Email email, LocalDate birthDate) {
		return this.email.equals(email) && this.birthDate.equals(birthDate);
	}	
	
	public void inactivateUser() {
		this.isActive = false;
	}
	
	public void setPassword(Password password) {
		this.password.setNewPassword(password);
	}
	
	public void setName(String name) {
		validateName(name);
		this.name = capitalizeName(name);
	}
	
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
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
	
	public LocalDate getCreationDate() {
		return creationDate;
	}
	
	public boolean getIsActive() {
		return isActive;
	}
	
	public TreeSet<Lane> getLanes() {
		return new TreeSet<Lane>(this.lanes);
	}
	
	public void setLaneToUser(Lane lane) {
		this.lanes.add(lane);
		
		Iterator<Lane> iterator = lanes.iterator();
	    while (iterator.hasNext()) {
	        Lane currentLane = iterator.next();
	      
	        if (iterator.hasNext()) {
	            Lane nextLane = iterator.next();
	            if (nextLane.getPosition() == currentLane.getPosition()) {
	                nextLane.setPosition(nextLane.getPosition() + 1);
	            }
	        }
	    }
	}
	
	public void removeLane(Lane lane) {
		this.lanes.remove(lane);
	}
	
	private void validateName(String name) {
		if (name != null && name.matches(REGEX_NAME)) {
			return;
		}
		throw new InvalidNameException();
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
