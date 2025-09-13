package com.planner.digitalPlanner.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Represents a task stored in the 'tasks' collection in MongoDB.
 * Each task is linked to a user and includes metadata like priority, category, and due date.
 */
@Document(collection = "tasks")
public class Task {

    @Id
    private String id;  // Unique identifier for the task document (MongoDB _id)

    private String userId;            // ID of the user who owns this task
    private String title;             // Title of the task
    private String description;       // Detailed description of the task
    private String priority;          // Priority level (e.g., "low", "medium", "high")
    private String categoryType;      // Category assigned to the task (e.g., "Work", "Personal")
    private LocalDateTime dueDate;    // Deadline for the task
    private boolean completed;        // Status flag: true if the task is completed

    /**
     * Constructor used to initialize a task with its properties.
     *
     * @param userId        the ID of the user to whom this task belongs
     * @param title         the title of the task
     * @param description   details about the task
     * @param priority      task priority: "low", "medium", or "high"
     * @param categoryType  category or tag associated with the task
     * @param dueDate       when the task is due
     * @param completed     true if the task is completed
     */
    public Task(String userId, String title, String description, String priority, String categoryType,
                LocalDateTime dueDate, boolean completed) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.categoryType = categoryType;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    // Getters and setters for each field

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
