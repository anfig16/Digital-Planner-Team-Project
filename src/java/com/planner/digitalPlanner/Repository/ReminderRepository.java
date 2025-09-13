package com.planner.digitalPlanner.Repository;

// Importing the Reminder model
import com.planner.digitalPlanner.Model.Reminder;

// Importing MongoRepository to access built-in CRUD operations
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository interface for Reminder entities.
 * Extends MongoRepository to provide standard and custom query methods.
 */
public interface ReminderRepository extends MongoRepository<Reminder, String> {

    /**
     * Finds all reminders associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of reminders belonging to the user.
     */
    List<Reminder> findByUserId(String userId);

    /**
     * Retrieves reminders associated with a specific task.
     *
     * @param taskId The ID of the task.
     * @return A list of reminders linked to the specified task.
     */
    List<Reminder> findByTaskId(String taskId);
    
    List<Reminder> findByUserEmail(String userEmail);
}
