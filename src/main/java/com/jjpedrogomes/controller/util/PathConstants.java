package com.jjpedrogomes.controller.util;

public enum PathConstants {
	
	TASK("com.jjpedrogomes.controller.task."),
	USER("com.jjpedrogomes.controller.auth."),
	LOGIN_SCREEN("/pages/login.jsp"),
	ACCOUNT_DETAILS("/WEB-INF/view/accountDetails.jsp"), 
	Lane("com.jjpedrogomes.controller.lane.");
	
	private final String path;

	PathConstants(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
