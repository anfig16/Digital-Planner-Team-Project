package com.planner.digitalPlanner.Controller;

import com.planner.digitalPlanner.Model.Reminder;
import com.planner.digitalPlanner.Repository.ReminderRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reminders") // Base URL for reminder-related endpoints
public class ReminderController {

    @Autowired
    private ReminderRepository reminderRepository; // Injecting the ReminderRepository to interact with MongoDB

    // Create a new reminder
    @PostMapping("/createReminder")
    public ResponseEntity<Reminder> createReminder(@RequestBody Reminder reminder, HttpSession session) {
    	String userEmail = (String) session.getAttribute("userEmail");
    	reminder.setUserEmail(userEmail);
    	
        // Save the new reminder and return it with status 200 OK
    	return ResponseEntity.ok(reminderRepository.save(reminder));
    }

    // Get reminders by userId
    @GetMapping("/getUserReminders/{userId}")
    public ResponseEntity<List<Reminder>> getRemindersByUserId(@PathVariable String userId) {
    	// Retrieve and return all reminders associated
    	List<Reminder> reminders = reminderRepository.findByUserId(userId);
        return ResponseEntity.ok(reminders);
    }
    
    //Associates reminders to current user account being used
    @GetMapping("/getUserReminders")
    public ResponseEntity<List<Reminder>> getRemindersByUserEmail(HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        List<Reminder> reminders = reminderRepository.findByUserEmail(userEmail);
        return ResponseEntity.ok(reminders);
    }

    // Get reminders by taskId
    @GetMapping("/getTaskReminder/{taskId}")
    public List<Reminder> getRemindersByTaskId(@PathVariable String taskId) {
        // Retrieve and return all reminders associated with the given taskId
        return reminderRepository.findByTaskId(taskId);
    }

    // Get a specific reminder by ID
    @GetMapping("/getReminder/{id}")
    public ResponseEntity<Reminder> getReminderById(@PathVariable String id) {
        // Find the reminder by ID and return it if found, otherwise return 404 Not Found
        return reminderRepository.findById(id)
                .map(ResponseEntity::ok) // Return 200 OK with the reminder if found
                .orElse(ResponseEntity.notFound().build()); // Return 404 if reminder not found
    }

 // Update a reminder (partial update)
    @PatchMapping("/updateReminder/{id}")
    public ResponseEntity<Reminder> updateReminder(@PathVariable String id, @RequestBody Reminder updatedReminder) {
        // Find the reminder by ID and update non-null fields
        Optional<Reminder> optionalReminder = reminderRepository.findById(id);

        if (optionalReminder.isPresent()) {
            Reminder reminder = optionalReminder.get();

            // Update the reminder fields only if provided in the request body
            if (updatedReminder.getReminderTime() != null)
                reminder.setReminderTime(updatedReminder.getReminderTime());

            if (updatedReminder.getTaskId() != null)
                reminder.setTaskId(updatedReminder.getTaskId());

            if (updatedReminder.getUserId() != null)
                reminder.setUserId(updatedReminder.getUserId());

            reminder.setEmailSent(updatedReminder.isEmailSent()); // 'false' is a valid value for emailSent

            // Save and return the updated reminder
            return ResponseEntity.ok(reminderRepository.save(reminder));
        } else {
            // Return 404 Not Found if reminder does not exist
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a reminder
    @DeleteMapping("/deleteReminder/{id}")
    public ResponseEntity<Void> deleteReminder(@PathVariable String id) {
        // Check if the reminder exists and delete it, otherwise return 404 Not Found
        if (reminderRepository.existsById(id)) {
            reminderRepository.deleteById(id);
            // Return 204 No Content after successful deletion
            return ResponseEntity.noContent().build();
        } else {
            // Return 404 Not Found if reminder doesn't exist
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/updateReminderByEmail/{id}")
    public ResponseEntity<Reminder> updateReminderByEmail(
            @PathVariable String id,
            @RequestBody Reminder updatedReminder,
            HttpSession session) {

    	String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Reminder> optionalReminder = reminderRepository.findById(id);
        if (optionalReminder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reminder reminder = optionalReminder.get();

        // Compare userEmail from the session with the userEmail in the reminder object
        if (!userEmail.equals(reminder.getUserEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Update fields if provided
        if (updatedReminder.getReminderTime() != null)
            reminder.setReminderTime(updatedReminder.getReminderTime());

        if (updatedReminder.getTitle() != null)
            reminder.setTitle(updatedReminder.getTitle());

        if (updatedReminder.getNotes() != null)
            reminder.setNotes(updatedReminder.getNotes());

        if (updatedReminder.getRepeat() != null)
            reminder.setRepeat(updatedReminder.getRepeat());

        reminder.setEmailSent(updatedReminder.isEmailSent());

        return ResponseEntity.ok(reminderRepository.save(reminder));
    }
    
    @DeleteMapping("/deleteReminderByEmail/{id}")
    public ResponseEntity<Void> deleteReminderByEmail(
            @PathVariable String id,
            HttpSession session) {

    	String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Reminder> optionalReminder = reminderRepository.findById(id);
        if (optionalReminder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reminder reminder = optionalReminder.get();

        // Compare userEmail from the session with the userEmail in the reminder object
        if (!userEmail.equals(reminder.getUserEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        reminderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
