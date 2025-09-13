package com.planner.digitalPlanner.Repository;

// Importing the Habit model
import com.planner.digitalPlanner.Model.Habit;

// Importing MongoRepository for basic CRUD operations
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository interface for Habit entities.
 * Provides CRUD functionality and custom queries for Habit documents in MongoDB.
 */
public interface HabitRepository extends MongoRepository<Habit, String> {

    /**
     * Finds all habit entries associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of habits belonging to the user.
     */
    List<Habit> findByUserId(String userId);
}
