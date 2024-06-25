package com.jjpedrogomes.model.shared;

public abstract class ModelException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private ModelErrorCode errorCode;
	
	public ModelException(ModelErrorCode errorCode) {
		super(errorCode.getLogMessage());
		this.errorCode = errorCode;
	}

	public ModelErrorCode getErrorCode() {
		return errorCode;
	}
}
