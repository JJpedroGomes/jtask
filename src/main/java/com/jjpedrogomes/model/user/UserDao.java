package com.jjpedrogomes.model.user;

import java.util.List;
import java.util.Optional;

public interface UserDao<T> {
	
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
    
    // Retrieves an entity by its email and password from the database.
    Optional<User> getUserByCredentials(String email, String password);
    
 // Retrieves an entity by its email and password from the database.
    Optional<User> getUserByEmail(String email);

}
