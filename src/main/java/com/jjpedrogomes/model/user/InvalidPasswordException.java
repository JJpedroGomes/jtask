package com.jjpedrogomes.model.user;

import com.jjpedrogomes.model.shared.ModelError;
import com.jjpedrogomes.model.shared.ModelException;

public class InvalidPasswordException extends ModelException {

	private static final long serialVersionUID = 1L;
	
	public InvalidPasswordException() {
		super(ModelError.INVALID_PASSWORD);
	}
}
