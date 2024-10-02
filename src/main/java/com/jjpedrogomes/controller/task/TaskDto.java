package com.jjpedrogomes.controller.task;

import com.jjpedrogomes.model.task.Task;

public class TaskDto {
	
	private String id;
	private String title;
	private String description;
	private String dueDate;
	private String conclusionDate;
	private String status;
	private String laneId;
	
	
	public TaskDto(Task task) {
		this.id = task.getId().toString();
		this.title = task.getTitle();
		this.description = task.getDescription();
		this.dueDate = task.getDueDate().toString();
		this.conclusionDate = task.getConclusionDate() != null ? task.getConclusionDate().toString() : null; 
		this.status = task.getStatus();
		this.laneId = task.getLane().getId().toString();
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getDueDate() {
		return dueDate;
	}


	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}


	public String getConclusionDate() {
		return conclusionDate;
	}


	public void setConclusionDate(String conclusionDate) {
		this.conclusionDate = conclusionDate;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getLaneId() {
		return laneId;
	}


	public void setLaneId(String laneId) {
		this.laneId = laneId;
	}
}
