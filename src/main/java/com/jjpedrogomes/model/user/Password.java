package com.jjpedrogomes.model.user;

import com.jjpedrogomes.model.shared.ValueObject;

public class Password implements ValueObject<Password>{
	
	private String content;
	private static final String REGEX_PASSWORD = "(?=.*[0-9])(?=.*[a-z]).{8,}";
	
	public Password (String content) {
		if (!isPasswordValid(content)) {
			throw new RuntimeException("Password does not match requirements");
		}
		this.content = content;
	}
	
	public void setNewPassword(Password password) {
		if (sameValueAs(password)) {
			throw new RuntimeException("The given password is already being used");
		}
		this.content = password.content;
	}
	
	private boolean isPasswordValid(String content) {
		return content != null && content.matches(REGEX_PASSWORD);
	}
		
	@Override
	public boolean sameValueAs(Password other) {
		return other != null && content.equals(other.content);
	}
	
	@Override
	public boolean equals(Object object) {
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
