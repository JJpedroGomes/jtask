package com.jjpedrogomes.model.lane;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.TreeSet;

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
		String name2 = "To do";
		String userName = "joão pedro";
		String address = "email@email.com";
		String password = "a1b2c3d4";
		
		User user = new User(userName, new Email(address), new Password(password), LocalDate.now());
		
		Lane lane = LaneFactory.createLane(name, user);
		Lane lane2 = LaneFactory.createLane(name2, user);
		
		assertThat(lane.getName()).isEqualTo(name);
		assertThat(lane.getPosition()).isEqualTo(0);
	
		assertThat(lane2.getName()).isEqualTo(user.getLanes().last().getName());
		assertThat(lane2.getPosition()).isEqualTo(1);
	}
	
	@Test
	void should_change_lane_position_for_user() {
		User user = new User("Lucas Nishida", new Email("email@email.com"), new Password("a1b2c3d4"), LocalDate.now());
		setUpLaneForSameUser(user);
		Lane newLane = LaneFactory.createLane("Finished", user);
		Integer desiredPosition = 1;
		
		newLane.switchTaskPosition(desiredPosition);
		
		Lane elementAtIndex = getElementAtIndex(user.getLanes(), desiredPosition);
		
		assertThat(newLane.getName()).isEqualTo(elementAtIndex.getName());
		assertThat(newLane.getPosition()).isEqualTo(desiredPosition);
	}
	
	void should_change_task_position_inside_lane() {

	}
	
	private Lane getElementAtIndex(TreeSet<Lane> set, Integer index) {
		return set.stream()
				.skip(index)
				.findFirst()
				.orElse(null);
	}
	
	private void setUpLaneForSameUser(User user) {
		LaneFactory.createLane("Backlog", user);
		LaneFactory.createLane("Todo", user);
		LaneFactory.createLane("Pending", user);
	}

	void should_remove_task_from_lane() {
		
	}
	
	void should_add_task_into_lane() {
		
	}
}
