package com.jjpedrogomes.model.shared;

public interface ValueObject<T> {

    boolean sameValueAs(T other);
}
