package com.planner.digitalPlanner.Repository;

// Importing the JournalEntry model
import com.planner.digitalPlanner.Model.JournalEntry;

// Importing MongoRepository to provide CRUD operations for MongoDB
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository interface for JournalEntry entities.
 * Inherits standard MongoDB operations from MongoRepository and defines custom queries.
 */
public interface JournalEntryRepository extends MongoRepository<JournalEntry, String> {

    /**
     * Retrieves all journal entries for a given user.
     *
     * @param userId The ID of the user.
     * @return A list of journal entries created by the user.
     */
    List<JournalEntry> findByUserId(String userId);
}
