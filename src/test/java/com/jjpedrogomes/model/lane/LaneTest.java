package com.jjpedrogomes.model.lane;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.user.Email;
import com.jjpedrogomes.model.user.Password;
import com.jjpedrogomes.model.user.User;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LaneTest {
	
	private User user = new User("Lucas Nishida", new Email("email@email.com"), new Password("a1b2c3d4"), LocalDate.now());

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
		setUpLaneForSameUser(user);
		Lane newLane = LaneFactory.createLane("Finished", user);
		Integer desiredPosition = 1;
		
		newLane.switchTaskPosition(desiredPosition);
		
		Lane elementAtIndex = getElementAtIndex(user.getLanes(), desiredPosition);
		
		assertThat(newLane.getName()).isEqualTo(elementAtIndex.getName());
		assertThat(newLane.getPosition()).isEqualTo(desiredPosition);
	}
	
	@Test
	void should_add_task_last_in_lane() {
		Lane newLane = LaneFactory.createLane("Backlog", user);
		Task firstTask = new Task("Do this", null, null);
		Task secondTask = new Task("Do that", null, null);

		newLane.addTaskLastToLane(firstTask);
		newLane.addTaskLastToLane(secondTask);
		List<Task> tasks = newLane.getTasks();
		
		assertThat(tasks.get(tasks.size() - 1).getTitle()).isEqualTo(secondTask.getTitle());
	}
	
	@Test
	void should_add_task_in_lane_position() {
		Lane newLane = LaneFactory.createLane("To Do", user);
		Task firstTask = new Task("Do this", null, null);
		Task secondTask = new Task("Do that", null, null);
		Task thirdTask = new Task("Do it", null, null);
		
		int index = 1;
		newLane.addTaskLastToLane(firstTask);
		newLane.addTaskLastToLane(secondTask);

		newLane.addTaskIntoLanesPosition(index, thirdTask);
		List<Task> tasks = newLane.getTasks();
		
		assertThat(tasks.get(index).getTitle()).isEqualTo(thirdTask.getTitle());
	}
	
	@Test
	void should_change_task_position_inside_lane() {
		Lane newLane = LaneFactory.createLane("To Do", user);
		Task firstTask = mock(Task.class);
		Task secondTask = mock(Task.class);
		Task thirdTask = mock(Task.class);
		
		when(firstTask.getId()).thenReturn(123L);
		when(secondTask.getId()).thenReturn(234L);
		when(thirdTask.getId()).thenReturn(356L);
		
		int desiredIndex = 0;
		newLane.addTaskLastToLane(firstTask);
		newLane.addTaskLastToLane(secondTask);
		newLane.addTaskLastToLane(thirdTask);
		
		newLane.changeTaskPosition(desiredIndex, thirdTask);
		List<Task> tasks = newLane.getTasks();
	
		assertThat(tasks).containsExactly(thirdTask, firstTask, secondTask);
	}
	
	@Test
	void should_throw_UnsupportedOperationException_trying_to_change_task_position() {
		Lane newLane = LaneFactory.createLane("To Do", user);
		Task firstTask = mock(Task.class);
		Task secondTask = mock(Task.class);
		Task thirdTask = mock(Task.class);
		
		when(firstTask.getId()).thenReturn(123L);
		when(secondTask.getId()).thenReturn(234L);
		when(thirdTask.getId()).thenReturn(356L);
		
		int desiredIndex = 0;
		newLane.addTaskLastToLane(firstTask);
		newLane.addTaskLastToLane(secondTask);
		
		assertThrows(UnsupportedOperationException.class, 
				() -> newLane.changeTaskPosition(desiredIndex, thirdTask));
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

	@Test
	void should_remove_task_from_lane() {
		Lane newLane = LaneFactory.createLane("To Do", user);
		Task task = mock(Task.class);
		
		newLane.removeTask(task);
		List<Task> tasks = newLane.getTasks();
		
		assertFalse(tasks.contains(task));
	}
}
