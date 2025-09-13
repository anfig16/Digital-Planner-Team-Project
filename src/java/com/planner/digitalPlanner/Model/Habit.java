package com.planner.digitalPlanner.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Represents a habit stored in the 'habits' collection in MongoDB.
 * A habit is associated with a user, and tracks if it was completed today, streak of consecutive completions, and the last completion date.
 */
@Document(collection = "habits")
public class Habit {

    @Id
    private String id;              // Unique MongoDB document ID

    private String userId;          // ID of the user who created this habit
    private String name;            // Name of the habit (e.g., "Drink water", "Exercise")

    private boolean completedToday; // Flag indicating if the habit was completed today
    private int streak;             // The number of consecutive days the habit has been completed
    private LocalDate lastCompletedDate; // Date when the habit was last completed, used to reset streak if missed

    /**
     * Constructor to create a new habit.
     *
     * @param userId                ID of the user who created the habit
     * @param name                  Name of the habit
     * @param completedToday        Whether the habit was completed today
     * @param streak                The number of consecutive days the habit has been completed
     * @param lastCompletedDate     The last date the habit was completed
     */
    public Habit(String userId, String name, boolean completedToday, int streak, LocalDate lastCompletedDate) {
        this.userId = userId;
        this.name = name;
        this.completedToday = completedToday;
        this.streak = streak;
        this.lastCompletedDate = lastCompletedDate;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompletedToday() {
        return completedToday;
    }

    public void setCompletedToday(boolean completedToday) {
        this.completedToday = completedToday;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public LocalDate getLastCompletedDate() {
        return lastCompletedDate;
    }

    public void setLastCompletedDate(LocalDate lastCompletedDate) {
        this.lastCompletedDate = lastCompletedDate;
    }
}
