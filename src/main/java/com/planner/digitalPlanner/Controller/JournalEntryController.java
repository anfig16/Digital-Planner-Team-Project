package com.planner.digitalPlanner.Controller;

import com.planner.digitalPlanner.Model.JournalEntry;
import com.planner.digitalPlanner.Repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal") // Base URL for journal entry-related endpoints
public class JournalEntryController {

    @Autowired
    private JournalEntryRepository journalEntryRepository; // Injecting the JournalEntryRepository to interact with MongoDB

    // Create a new journal entry
    @PostMapping("/createJournalEntry")
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry journalEntry) {
        // Save the new journal entry and return it with status 200 OK
        return ResponseEntity.ok(journalEntryRepository.save(journalEntry));
    }

    // Get all journal entries by userId
    @GetMapping("/getJournalEntries/{userId}")
    public List<JournalEntry> getJournalEntriesByUserId(@PathVariable String userId) {
        // Retrieve and return all journal entries associated with the given userId
        return journalEntryRepository.findByUserId(userId);
    }

    // Get a specific journal entry by ID
    @GetMapping("/getJournalEntry/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable String id) {
        // Find the journal entry by ID and return it if found, otherwise return 404 Not Found
        return journalEntryRepository.findById(id)
                .map(ResponseEntity::ok) // Return 200 OK with the journal entry if found
                .orElse(ResponseEntity.notFound().build()); // Return 404 if journal entry not found
    }

    // Update a journal entry (partial update)
    @PatchMapping("/updateJournalEntry/{id}")
    public ResponseEntity<JournalEntry> updateJournalEntry(@PathVariable String id, @RequestBody JournalEntry updatedEntry) {
        // Find the journal entry by ID and update non-null fields
        Optional<JournalEntry> optionalEntry = journalEntryRepository.findById(id);

        if (optionalEntry.isPresent()) {
            JournalEntry entry = optionalEntry.get();

            // Update the journal entry fields only if provided in the request body
            if (updatedEntry.getTitle() != null)
                entry.setTitle(updatedEntry.getTitle());

            if (updatedEntry.getContent() != null)
                entry.setContent(updatedEntry.getContent());

            if (updatedEntry.getDate() != null)
                entry.setDate(updatedEntry.getDate());

            if (updatedEntry.getUserId() != null)
                entry.setUserId(updatedEntry.getUserId());

            // Save and return the updated journal entry
            return ResponseEntity.ok(journalEntryRepository.save(entry));
        } else {
            // Return 404 Not Found if journal entry does not exist
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a journal entry
    @DeleteMapping("/deleteJournalEntry/{id}")
    public ResponseEntity<Void> deleteJournalEntry(@PathVariable String id) {
        // Check if the journal entry exists and delete it, otherwise return 404 Not Found
        if (journalEntryRepository.existsById(id)) {
            journalEntryRepository.deleteById(id);
            // Return 204 No Content after successful deletion
            return ResponseEntity.noContent().build();
        } else {
            // Return 404 Not Found if journal entry doesn't exist
            return ResponseEntity.notFound().build();
        }
    }
}
