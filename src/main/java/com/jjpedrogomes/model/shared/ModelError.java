package com.jjpedrogomes.model.shared;

public enum ModelError {
	
	INVALID_EMAIL(1001, "Invalid email address provided"),
	INVALID_PASSWORD(1002, "Password does not match requirements");

	private final Integer code;
    private final String logMessage;
    
	private ModelError(int code, String logMessage) {
		this.code = code;
		this.logMessage = logMessage;
	}

	public Integer getCode() {
		return code;
	}

	public String getLogMessage() {
		return logMessage;
	}
}
