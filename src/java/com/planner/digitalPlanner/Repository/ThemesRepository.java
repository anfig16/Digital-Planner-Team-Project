package com.planner.digitalPlanner.Repository;

// Importing the Themes model
import com.planner.digitalPlanner.Model.Themes;

// Importing MongoRepository to leverage built-in CRUD functionality
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository interface for Themes entities.
 * Extends MongoRepository to provide standard CRUD operations for Themes documents.
 */
public interface ThemesRepository extends MongoRepository<Themes, String> {

    /**
     * Finds all themes that are marked as default.
     * Spring Data interprets method name to generate the appropriate query.
     *
     * @return A list of default Themes.
     */
    List<Themes> findByIsDefaultTrue();

    /**
     * Retrieves all themes associated with a specific user by userId.
     *
     * @param userId The ID of the user.
     * @return A list of Themes belonging to the specified user.
     */
    List<Themes> findByUserId(String userId);
}
