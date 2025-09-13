package com.planner.digitalPlanner.Repository;

// Importing the Category model
import com.planner.digitalPlanner.Model.Category;

// Importing MongoRepository for built-in CRUD functionality
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository interface for Category entities.
 * Extends MongoRepository to provide standard CRUD operations and custom queries.
 */
public interface CategoryRepository extends MongoRepository<Category, String> {

    /**
     * Retrieves a list of categories associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of Category objects belonging to the user.
     */
    List<Category> findByUserId(String userId);
}
