package com.planner.digitalPlanner.Model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a category associated with a user, used to categorize tasks, reminders, etc.
 * Stored in the 'categories' collection in MongoDB.
 */
@Document(collection = "categories")
public class Category {

    @Id
    private String id;              // Unique MongoDB document ID

    private String userId;          // ID of the user to whom this category belongs
    private String label;           // Name or label of the category (e.g., "Work", "Personal")
    private String colorHex;        // Hex color code for the category (e.g., "#FF5733")

    /**
     * Constructor to create a new category.
     *
     * @param userId    ID of the user who created the category
     * @param label     Label or name of the category
     * @param colorHex  Hex color code to represent the category
     */
    public Category(String userId, String label, String colorHex) {
        this.userId = userId;
        this.label = label;
        this.colorHex = colorHex;
    }

    // Getters and Setters

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }
}
