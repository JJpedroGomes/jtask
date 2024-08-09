package com.jjpedrogomes.model.lane;

import java.util.List;
import java.util.Optional;

public interface LaneDao {
	
	// Retrieves an entity by its ID from the database.
    Optional<Lane> get(long id);

    List<Lane> getAllFromUser(Long id);

    // Saves a new entity to the database.
    void save(Lane t);

    // Updates an existing entity in the database based on the provided parameters.
    Lane update(Lane t);

    // Deletes an entity from the database.
    void delete(Lane t);
}
