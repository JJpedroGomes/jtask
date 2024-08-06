package com.jjpedrogomes.model.lane;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.jjpedrogomes.model.user.Email;
import com.jjpedrogomes.model.user.Password;
import com.jjpedrogomes.model.user.User;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LaneTest {

	@Test
	void should_create_lane_with_the_last_position_sucessfully() {
		String name = "Backlog";
		String userName = "joão pedro";
		String address = "email@email.com";
		String password = "a1b2c3d4";
		
		User user = new User(userName, new Email(address), new Password(password), LocalDate.now());
		Lane lane = LaneFactory.createLane(name, user);
		
		assertThat(lane.getName()).isEqualTo(name);
		assertThat(lane).isEqualTo(user.getLanes().last());
		assertThat(lane.getPosition()).isEqualTo(1);
	}
	
	void should_change_task_position_inside_lane() {
		
	}
	
	void should_remove_task_from_lane() {
		
	}
	
	void should_add_task_into_lane() {
		
	}
}
