package com.jjpedrogomes.model.lane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

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
	
	public void switchTaskPosition(Integer desiredPosition) {	
		TreeSet<Lane> lanes = this.user.getLanes();
		lanes.remove(this);
		
		// if desired position is greater than lanes size, put it in last
		if(desiredPosition > lanes.size()) {
			desiredPosition = lanes.size();
		}
			
		this.setPosition(desiredPosition);
		lanes.add(this);
	}

	public void addTaskLastToLane(Task newTask) {
		this.tasks.add(newTask);
	}
	
	public void addTaskIntoLanesPosition(int index, Task thirdTask) {
		this.tasks.add(index, thirdTask);
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
