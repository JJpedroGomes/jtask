package com.jjpedrogomes.model.task;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.jjpedrogomes.model.lane.Lane;
import com.jjpedrogomes.model.shared.Entity;

/**
 * Represents a task in the system.
 */
@javax.persistence.Entity
@Table(name = "task")
public class Task implements Entity<Task> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_sequence")
    @SequenceGenerator(name = "task_sequence", sequenceName = "task_sequence", allocationSize = 1)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "creation_date", columnDefinition = "DATE", nullable = false)
    private LocalDate creationDate;
    @Column(name = "due_date", columnDefinition = "DATE")
    private LocalDate dueDate;
    @Column(name = "conclusion_date", columnDefinition = "DATE")
    private LocalDate conclusionDate;
    @Embedded
    @AttributeOverride(name = "current", column = @Column(name = "status", nullable = false))
    private Status status;
    @ManyToOne
    @JoinColumn(name = "lane_id", nullable = false)
    private Lane lane;
    @Column(nullable = false)
	private Integer position;

    @Transient
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    /**
     * Constructs an instance of the Task class with the provided parameters.
     * @param title represents the main goal of the created task
     * @param description represents the goal with more details; this field can be null
     * @param dueDate represents the due date of the created task; this field can be null
     *        If null, the task is considered not to have a due date.
     *        Otherwise, it represents the date by which the task should be completed.
     *        Tasks without a due date are considered ongoing tasks.
     * @implNote The creationDate field is automatically set to the current date when the task is created.
     *           The status field is determined based on the due date:
     *           - If the due date is in the past, the task status is set to PENDING.
     *           - Otherwise, the task status is IN_PROGRESS.
     */
    public Task(String title, String description, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.creationDate = LocalDate.now();
        this.dueDate = dueDate;
        this.status = (dueDate == null ? new Status() : new Status(dueDate));
        this.position = 0;
        //this.lane = lane;
        //lane.addTaskLastToLane(this);
    }

    /**
     * Marks the task as completed.
     * If the task is already marked as completed, no action is taken.
     * Otherwise, sets the status of the task to completed and records the conclusion date as the current date.
     */
    public void setTaskCompleted() {
        if (this.status.getCurrentStatus().equals(Status.COMPLETED)) return;
        this.status.setStatusToCompleted();
        this.conclusionDate = LocalDate.now();
    }

    public LocalDate getConclusionDate() {
        return conclusionDate;
    }

    /**
     * Marks the task as in progress.
     * Resets the conclusion date to null.
     * If the task has a due date, and it's already past the current date,
     * sets the task status to pending; otherwise, sets it to in progress.
     */
    public void setTaskInProgress() {
        this.conclusionDate = null;
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            this.status.setStatusPending();
        } else {
            this.status.setStatusInProgress();
        }
    }

    /**
     * Sets the due date of the task and updates its status accordingly.
     * After setting the due date, the task status is updated:
     * - If the due date is in the past, the task status is set to pending.
     * - Otherwise, the task status is set to in progress.
     * If the task was previously marked as completed, this method reverts its completion status.
     * @param dueDate The due date to be set for the task.
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        setTaskInProgress();
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status.getCurrentStatus();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title.isEmpty()) return;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }
    
    public void setLane(Lane lane) {
    	this.lane = lane;
    }
    
    public Lane getLane() {
    	return this.lane;
    }
    
    public boolean isCompleted() {
        return this.getStatus().equals(Status.COMPLETED);
    }
    
    public void setPosition(int position) {
    	this.position = position;
    }
    
    public Integer getPosition() {
    	return this.position;
    }

    @Override
    public boolean sameIdentityAs(final Task other) {
        return other != null && id.equals(other.id);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final Task other = (Task) object;
        return sameIdentityAs(other);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + (description != null ? description : "null") + '\'' +
                ", creationDate=" + formatDate(creationDate) +
                ", dueDate=" + formatDate(dueDate) +
                ", conclusionDate=" + formatDate(conclusionDate) +
                ", status=" + status.getCurrentStatus() +
                '}';
    }

    private String formatDate(LocalDate date) {
        return (date != null) ? date.format(dateFormatter) : "null";
    }

    public Task() {
        //Needed By hibernate
    }
}
