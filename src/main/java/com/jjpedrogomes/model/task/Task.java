package com.jjpedrogomes.model.task;


import com.jjpedrogomes.model.shared.Entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task in the system.
 */
@javax.persistence.Entity
@Table(name = "task")
public class Task implements Entity<Task> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @AttributeOverride(name = "current", column = @Column(name = "status"))
    private Status status;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Task(String title, String description, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.creationDate = LocalDate.now();
        this.dueDate = dueDate;
        this.status = (dueDate == null ? new Status() : new Status(dueDate));
    }

    public void setTaskCompleted() {
        if (this.status.getCurrentStatus().equals(Status.COMPLETED)) return;
        this.status.setStatusToCompleted();
        this.conclusionDate = LocalDate.now();
    }

    public LocalDate getConclusionDate() {
        return conclusionDate;
    }

    public void setTaskInProgress() {
        this.conclusionDate = null;
        //if (this.status.equals(Status.IN_PROGRESS) || this.status.equals(Status.PENDING)) return;
        if (dueDate == null || dueDate.isAfter(LocalDate.now()) || dueDate.isEqual(LocalDate.now())){
            this.status.setStatusInProgress();
            return;
        }
        if (dueDate.isBefore(LocalDate.now())) this.status.setStatusPending();
    }

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
