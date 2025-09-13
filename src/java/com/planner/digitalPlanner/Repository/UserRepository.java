package com.planner.digitalPlanner.Repository;

// Importing the User model
import com.planner.digitalPlanner.Model.User;

// Importing Spring Data MongoDB repository support
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for User entities.
 * Extends MongoRepository to provide basic CRUD operations for User documents.
 */
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Custom query method to find a user by their email address.
     * Spring Data will automatically implement this query based on the method name.
     *
     * @param email The email of the user to search for.
     * @return The User object matching the given email, or null if not found.
     */
    User findByEmail(String email);
}
