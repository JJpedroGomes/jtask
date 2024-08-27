package com.jjpedrogomes.controller.lane;

import com.jjpedrogomes.model.lane.Lane;

public class LaneDto {
	
	private Long id;
	private String name;
	
	public LaneDto(Lane lane) {
		this.id = lane.getId();
		this.name = lane.getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LaneDto() {}
}
