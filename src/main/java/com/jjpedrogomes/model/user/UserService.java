package com.jjpedrogomes.model.user;

import java.time.LocalDate;

public interface UserService {
	
	User createUser(String name, String email, String password, LocalDate birthDate);
	
	User updateUser(UserUpdateRequest userUpdateRequest);
}
