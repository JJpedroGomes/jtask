package com.jjpedrogomes.model.task;

import com.jjpedrogomes.model.shared.ValueObject;

import javax.persistence.Embeddable;
import java.time.LocalDate;

/**
 * Represents a task status
 */
@Embeddable
public class Status implements ValueObject<Status> {

    private String current;
    public static final String IN_PROGRESS = "In Progress";
    public static final String PENDING = "Pending";
    public static final String COMPLETED = "Completed";

    /**
     * Constructs a new Status instance based on the provided due date.
     * Initializes the status of the task based on whether the due date is in the past.
     * If the due date is in the past, the status is set to PENDING.
     * Otherwise, the status is set to IN_PROGRESS.
     * @param dueDate The due date for completing the associated task.
     */
    public Status(LocalDate dueDate) {
        if (dueDate.isBefore(LocalDate.now())) {
            this.current = PENDING;
        } else {
            this.current = IN_PROGRESS;
        }
    }

    public Status() {
        this.current = IN_PROGRESS;
    }

    public void setStatusInProgress() {
        this.current = IN_PROGRESS;
    }

    public void setStatusPending() {
        this.current = PENDING;
    }

    public void setStatusToCompleted() {
        this.current = COMPLETED;
    }

    public String getCurrentStatus() {
        return current;
    }

    @Override
    public boolean sameValueAs(final Status other) {
        return other != null && current.equals(other.current);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Status other = (Status) object;
        return sameValueAs(other);
    }

    @Override
    public int hashCode() {
        return current.hashCode();
    }

    @Override
    public String toString() {
        return "Status{" +
                "current='" + current + '\'' +
                '}';
    }
}
