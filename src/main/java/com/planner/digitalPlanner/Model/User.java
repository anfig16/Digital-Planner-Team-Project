package com.planner.digitalPlanner.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a User entity stored in the 'users' collection in MongoDB.
 * Includes user-specific information such as name, email, and password.
 */
@Document(collection = "users")
public class User {

    @Id
    private String id;          // Unique identifier for the user (MongoDB _id)

    private String name;        // User's display name
    private String email;       // User's email address (used for login)
    private String password;    // User's encrypted password

    /**
     * Default no-args constructor required by Spring Data.
     */
    public User() {}

    /**
     * Constructor for creating a user with essential fields.
     *
     * @param name     the user's name
     * @param email    the user's email
     * @param password the user's password
     */
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters and setters for all fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
