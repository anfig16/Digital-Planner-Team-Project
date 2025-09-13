package com.planner.digitalPlanner.Controller;

import com.planner.digitalPlanner.Model.Habit;
import com.planner.digitalPlanner.Repository.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/habits") // Base URL for habit-related endpoints
public class HabitController {

    @Autowired
    private HabitRepository habitRepository; // Injecting HabitRepository to interact with the database

    // Create a new habit
    @PostMapping("/createHabit")
    public ResponseEntity<Habit> createHabit(@RequestBody Habit habit) {
        // Save the new habit and return it with status 200 OK
        return ResponseEntity.ok(habitRepository.save(habit));
    }

    // Get all habits by userId
    @GetMapping("/getHabits/{userId}")
    public List<Habit> getHabitsByUserId(@PathVariable String userId) {
        // Retrieve and return all habits associated with the given userId
        return habitRepository.findByUserId(userId);
    }

    // Get a specific habit by ID
    @GetMapping("/getHabit/{id}")
    public ResponseEntity<Habit> getHabitById(@PathVariable String id) {
        // Find the habit by ID and return it if found, otherwise return 404 Not Found
        return habitRepository.findById(id)
                .map(ResponseEntity::ok) // Return 200 OK with the habit if found
                .orElse(ResponseEntity.notFound().build()); // Return 404 if habit not found
    }

    // Update a habit (partial update)
    @PatchMapping("/updateHabit/{id}")
    public ResponseEntity<Habit> updateHabit(@PathVariable String id, @RequestBody Habit updatedHabit) {
        // Find the habit by ID and update non-null fields
        Optional<Habit> optionalHabit = habitRepository.findById(id);

        if (optionalHabit.isPresent()) {
            Habit habit = optionalHabit.get();

            // Update the habit fields only if provided in the request body
            if (updatedHabit.getName() != null)
                habit.setName(updatedHabit.getName());

            habit.setCompletedToday(updatedHabit.isCompletedToday());
            habit.setStreak(updatedHabit.getStreak());

            if (updatedHabit.getLastCompletedDate() != null)
                habit.setLastCompletedDate(updatedHabit.getLastCompletedDate());

            if (updatedHabit.getUserId() != null)
                habit.setUserId(updatedHabit.getUserId());

            // Save and return the updated habit
            return ResponseEntity.ok(habitRepository.save(habit));
        } else {
            // Return 404 Not Found if habit does not exist
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a habit
    @DeleteMapping("/deleteHabit/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable String id) {
        // Check if the habit exists and delete it, otherwise return 404 Not Found
        if (habitRepository.existsById(id)) {
            habitRepository.deleteById(id);
            // Return 204 No Content after successful deletion
            return ResponseEntity.noContent().build();
        } else {
            // Return 404 Not Found if habit doesn't exist
            return ResponseEntity.notFound().build();
        }
    }
}
