package com.jjpedrogomes.controller.auth;

import com.google.gson.annotations.Expose;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.user.UserUpdateRequest;

public class UserDto implements UserUpdateRequest{
	
	@Expose
	private Long id;
	@Expose
	private String name;
	@Expose
	private String email;
	private String password1;
	private String password2;
	@Expose
	private String birthDate;
	@Expose
	private String creationDate;
	@Expose
	private boolean isActive;
	
	public UserDto(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail().toString();
		this.birthDate = user.getBirthDate().toString();
		this.creationDate = user.getCreationDate().toString();
		this.isActive = user.getIsActive();
	}
	
	public UserDto() {}
	
	@Override
	public Long getId() {
		return id;
	}
	
	public UserDto setId(Long id) {
		this.id = id;
		return this;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public UserDto setName(String name) {
		this.name = name;
		return this;
	}
	
	@Override
	public String getEmail() {
		return email;
	}
	
	public UserDto setEmail(String email) {
		this.email = email;
		return this;
	}
	
	@Override
	public String getBirthDate() {
		return birthDate;
	}

	public UserDto setBirthDate(String birthDate) {
		this.birthDate = birthDate;
		return this;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public UserDto setCreationDate(String creationDate) {
		this.creationDate = creationDate;
		return this;
	}


	public boolean isActive() {
		return isActive;
	}
	
	public UserDto setActive(boolean isActive) {
		this.isActive = isActive;
		return this;
	}

	@Override
	public String getPassword1() {
		return password1;
	}


	public UserDto setPassword1(String password1) {
		this.password1 = password1;
		return this;
	}

	@Override
	public String getPassword2() {
		return password2;
	}


	public UserDto setPassword2(String password2) {
		this.password2 = password2;
		return this;
	}	
}
