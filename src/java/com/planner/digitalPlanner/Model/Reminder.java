package com.planner.digitalPlanner.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Represents a reminder stored in the 'reminders' collection in MongoDB.
 * Reminders are associated with specific tasks and users.
 */
@Document(collection = "reminders")
public class Reminder {

    @Id
    private String id;                      // Unique MongoDB document ID
    private String userId;                  // ID of the user this reminder belongs to
    private String taskId;                  // ID of the task this reminder is for
    private LocalDateTime reminderTime;     // Time when the reminder should trigger
    private boolean emailSent;              // Indicates if an email notification has been sent
    private String title;
    private String notes;
    private String repeat;
    private String userEmail; 

    /**
     * Constructor to create a new reminder.
     *
     * @param userId        ID of the user to whom the reminder belongs
     * @param taskId        ID of the task linked to the reminder
     * @param reminderTime  Scheduled time for the reminder
     * @param emailSent     True if the email has been sent, false otherwise
     */
    public Reminder(String userId, String taskId, LocalDateTime reminderTime, boolean emailSent) {
        this.userId = userId;
        this.taskId = taskId;
        this.reminderTime = reminderTime;
        this.emailSent = emailSent;
    }


    // Getters and setters

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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public boolean isEmailSent() {
        return emailSent;
    }

    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getRepeat() {
		return repeat;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public String getUserEmail() {
	    return userEmail;
	}

	public void setUserEmail(String userEmail) {
	    this.userEmail = userEmail;
	}
}
