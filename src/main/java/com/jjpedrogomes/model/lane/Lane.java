package com.jjpedrogomes.model.lane;

import java.util.Collection;
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
	private Collection<Task> tasks = new TreeSet<Task>();
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	@Column(nullable = false)
	private Integer position;
	
	protected Lane(String name, User user) {
		this.name = name;
		this.user = user;
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
	
	public Lane() {}

	@Override
	public int compareTo(Lane other) {
		if (this.position == null && other.getPosition() != null) {
	        return -1;
	    }
	    else if (this.position != null && other.getPosition() == null) {
	        return 1;
	    } else if (this.position == null && other.getPosition() == null ) {
	    	return 0;
	    }
		
		return Integer.compare(this.position, other.getPosition());
	}
}
