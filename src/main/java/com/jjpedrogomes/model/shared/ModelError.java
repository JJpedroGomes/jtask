package com.jjpedrogomes.model.shared;

public enum ModelErrorCode {
	
	INVALID_EMAIL(1001, "Invalid email address provided");

	private final Integer code;
    private final String logMessage;
    
	private ModelErrorCode(int code, String logMessage) {
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
