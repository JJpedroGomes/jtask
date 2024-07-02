package com.jjpedrogomes.model.user;

import com.jjpedrogomes.model.shared.ModelError;
import com.jjpedrogomes.model.shared.ModelException;

public class InvalidNameException extends ModelException {
	
	private static final long serialVersionUID = 1L;

	public InvalidNameException() {
		super(ModelError.INVALID_NAME);
	}

}
