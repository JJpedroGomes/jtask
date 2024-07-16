package com.jjpedrogomes.controller.auth;

import com.jjpedrogomes.model.user.User;

public class UserDto {
	
	private Long id;
	private String name;
	private String email;
	private String birthDate;
	private boolean isActive;
	
	public UserDto(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail().toString();
		this.birthDate = user.getBirthDate().toString();
		this.isActive = user.getIsActive();
	}
	
	 
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public boolean isActive() {
		return isActive;
	}
	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}
