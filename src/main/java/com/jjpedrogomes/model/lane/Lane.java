package com.jjpedrogomes.model.lane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.user.User;

@Entity
public class Lane implements com.jjpedrogomes.model.shared.Entity<Lane>, Comparable<Lane>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lane_sequence")
	@SequenceGenerator(name = "lane_sequence", sequenceName = "lane_sequence", allocationSize = 1)
	private Long id;
	@Column(nullable = false)
	private String name;
	@OneToMany(mappedBy = "lane", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Task> tasks = new ArrayList<Task>();
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	@Column(nullable = false)
	private Integer position;
	
	protected Lane(String name, User user) {
		this.name = name;
		this.user = user;
	}
	
	/**
	 * Switches the position of this task within the user's lanes.
	 * 
	 * @param desiredIndex the new position index for this task within the user's lanes.
	 *                      If the index is greater than the current number of lanes, 
	 *                      the task will be moved to the last position.
	 */
	public void switchTaskPosition(Integer desiredIndex) {	
//		Set<Lane> lanes = this.user.getLanes();
		int size = this.user.getLanes().size();
		this.user.removeLane(this);
		
		// if desired position is greater than lanes size, put it in last
		if(desiredIndex > size) {
			desiredIndex = size;
		}
			
		this.setPosition(desiredIndex);
		this.user.setLaneToUser(this);
	}
	
	/**
	 * Changes the position of a specific task within the current list of tasks.
	 * 
	 * @param desiredIndex the new position index for the task within the list of tasks.
	 * @param task the task to be repositioned.
	 * @throws UnsupportedOperationException if the task is not found in the list of tasks.
	 */
	public void changeTaskPosition(int desiredIndex, Task task) {
		int currentIndex = this.tasks.indexOf(task);
		if (currentIndex == -1) {
	        throw new UnsupportedOperationException();
	    }
		this.tasks.remove(currentIndex);
		this.tasks.add(desiredIndex, task);
	}
	
	/**
	 * Removes a specific task from the list of tasks.
	 * 
	 * @param task the task to be removed from the list.
	 */
	public void removeTask(Task task) {
		this.tasks.remove(task);
	}

	/**
	 * Adds a new task to the end of the list of tasks within the current lane.
	 * 
	 * @param newTask the task to be added to the end of the list.
	 */
	public void addTaskLastToLane(Task newTask) {
		this.tasks.add(newTask);
		newTask.setLane(this);
	}
	
	/**
	 * Adds a new task at a specific position within the list of tasks.
	 * 
	 * @param index the position index at which to insert the new task.
	 * @param thirdTask the task to be added at the specified index.
	 */
	public void addTaskIntoLanesPosition(int index, Task newTask) {
		this.tasks.add(index, newTask);
		newTask.setLane(this);
	}
	
	public Integer getPosition() {
		return position;
	}
	
	public void setPosition(Integer position) {
		this.position = position;
	}
	
	public String getName() {
		return name;
	}
	
	public User getUser() {
		return user;
	}
	
	public List<Task> getTasks() {
		return Collections.unmodifiableList(this.tasks);
	}
	
	public Long getId() {
		return id;
	}
	
	@Override
	public boolean sameIdentityAs(Lane other) {
		return other != null && id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Lane other = (Lane) obj;
		return sameIdentityAs(other);
	}

	@Override
	public int compareTo(Lane other) {
		if (other.getPosition() == null) {
	        return 1;
	    }
		if (this.getPosition() == null) {
	        return -1;
	    }
		
		// Regular comparison based on position
	    int positionComparison = Integer.compare(this.position, other.getPosition());
	    
	    // If positions are equal, make sure to treat the new Lane as greater
	    return positionComparison != 0 ? positionComparison : -1;
	}

	@Override
	public String toString() {
		return "Lane [id=" + id + ", name=" + name + ", user=" + user + ", position=" + position + "]";
	}
	
	public Lane() {}
}
