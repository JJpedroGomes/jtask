package com.jjpedrogomes.model.shared;

public interface Entity<T> {

    boolean sameIdentityAs(T other);
}
