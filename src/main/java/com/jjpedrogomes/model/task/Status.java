package com.jjpedrogomes.model.task;

import com.jjpedrogomes.model.shared.ValueObject;

import javax.persistence.Embeddable;

/**
 * Represents a task status
 */
@Embeddable
public class Status implements ValueObject<Status> {

    private String current;
    private static final String IN_PROGRESS = "In Progress";
    private static final String PENDING = "Pending";
    private static final String COMPLETED = "Completed";

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
