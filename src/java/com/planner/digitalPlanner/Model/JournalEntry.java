package com.planner.digitalPlanner.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Represents a journal entry stored in the 'journal_entries' collection in MongoDB.
 * A journal entry is associated with a user and includes a title, content, and date.
 */
@Document(collection = "journal_entries")
public class JournalEntry {

    @Id
    private String id;              // Unique MongoDB document ID

    private String userId;          // ID of the user who created this journal entry
    private String title;           // Title of the journal entry
    private String content;         // Content or text of the journal entry
    private LocalDate date;         // Date when the journal entry was created

    /**
     * Constructor to create a new journal entry.
     *
     * @param userId    ID of the user who created the journal entry
     * @param title     Title of the journal entry
     * @param content   Content of the journal entry
     * @param date      Date when the journal entry was created
     */
    public JournalEntry(String userId, String title, String content, LocalDate date) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.date = date;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
