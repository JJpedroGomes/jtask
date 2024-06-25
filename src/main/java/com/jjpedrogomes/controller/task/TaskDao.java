package com.jjpedrogomes.controller.task;

import java.util.List;
import java.util.Optional;

// This interface represents a Data Access Object (DAO) contract for managing entities in the database.
public interface TaskDao<T> {

    // Retrieves an entity by its ID from the database.
    Optional<T> get(long id);

    // Retrieves all entities from the database.
    List<T> getAll();

    // Saves a new entity to the database.
    void save(T t);

    // Updates an existing entity in the database based on the provided parameters.
    T update(T t);

    // Deletes an entity from the database.
    void delete(T t);
}
