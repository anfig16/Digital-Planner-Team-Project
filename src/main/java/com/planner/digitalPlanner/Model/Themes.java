package com.planner.digitalPlanner.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a visual theme stored in the 'themes' collection in MongoDB.
 * Themes can be user-specific or default for all users.
 */
@Document(collection = "themes")
public class Themes {

    @Id
    private String id;  // Unique identifier for the theme document (MongoDB _id)

    private String userId;            // Null if it's a global theme; otherwise, links the theme to a specific user
    private String name;              // Human-readable name for the theme (e.g., "Dark Mode", "Ocean Blue")
    private String primaryColor;      // Main accent color used throughout the app's UI
    private String secondaryColor;    // Secondary accent color used for highlights, borders, etc.
    private String backgroundImageUrl;// URL to a background image used in the theme
    private String textColor;         // Color used for primary text elements
    private boolean isDefault;        // Indicates if the theme is a built-in preset available to all users

    /**
     * Constructor used for creating new theme instances.
     *
     * @param userId              associated user ID or null for default themes
     * @param name                name of the theme
     * @param primaryColor        primary color hex code
     * @param secondaryColor      secondary color hex code
     * @param backgroundImageUrl  URL to a background image
     * @param textColor           hex code for text color
     * @param isDefault           true if the theme is a global preset
     */
    public Themes(String userId, String name, String primaryColor, String secondaryColor, String backgroundImageUrl, String textColor, boolean isDefault) {
        this.userId = userId;
        this.name = name;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.backgroundImageUrl = backgroundImageUrl;
        this.textColor = textColor;
        this.isDefault = isDefault;
    }

    // Getters and Setters for all fields

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
