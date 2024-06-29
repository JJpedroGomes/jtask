package com.jjpedrogomes.model.shared;

public abstract class ModelException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private ModelError errorCode;
	
	public ModelException(ModelError errorCode) {
		super(errorCode.getLogMessage());
		this.errorCode = errorCode;
	}

	public ModelError getErrorCode() {
		return errorCode;
	}
}
