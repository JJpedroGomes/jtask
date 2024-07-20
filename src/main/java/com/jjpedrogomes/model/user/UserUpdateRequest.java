package com.jjpedrogomes.model.user;

public interface UserUpdateRequest {
	Long getId();
    String getName();
    String getEmail();
    String getPassword1();
    String getPassword2();
    String getBirthDate();
}
