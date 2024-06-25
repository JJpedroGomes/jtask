package com.jjpedrogomes.controller.util;

public enum PathConstants {
	
	TASK("com.jjpedrogomes.controller.task."),
	USER("com.jjpedrogomes.controller.auth.");
	
	private final String path;

	PathConstants(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
