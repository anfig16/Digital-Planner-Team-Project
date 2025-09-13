package com.planner.digitalPlanner.Repository;

// Importing the Task model
import com.planner.digitalPlanner.Model.Task;

// Importing MongoRepository to enable CRUD operations
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository interface for Task entities.
 * Inherits CRUD and query methods from MongoRepository.
 */
public interface TaskRepository extends MongoRepository<Task, String> {

    /**
     * Retrieves all tasks associated with a specific user.
     * Spring Data will automatically create the query based on method naming.
     *
     * @param userId The ID of the user whose tasks are to be fetched.
     * @return A list of tasks belonging to the specified user.
     */
    List<Task> findByUserId(String userId);

}
