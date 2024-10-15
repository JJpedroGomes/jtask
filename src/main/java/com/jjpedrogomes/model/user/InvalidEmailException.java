package com.jjpedrogomes.model.user;

import com.jjpedrogomes.model.shared.ModelError;
import com.jjpedrogomes.model.shared.ModelException;

public class InvalidEmailException extends ModelException {

	private static final long serialVersionUID = 1L;

	public InvalidEmailException() {
		super(ModelError.INVALID_EMAIL);
	}
}