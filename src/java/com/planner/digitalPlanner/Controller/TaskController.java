package com.planner.digitalPlanner.Controller;

import com.planner.digitalPlanner.Model.Task;
import com.planner.digitalPlanner.Repository.TaskRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks") // Base URL for task-related endpoints
public class TaskController {

    @Autowired
    private TaskRepository taskRepository; // Injecting the TaskRepository to interact with MongoDB

    // Create a new task
    @PostMapping("/createTask")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        // Save the new task and return the saved task with status 200 OK
        return ResponseEntity.ok(taskRepository.save(task));
    }

    // Get all tasks for a user by userID
    @GetMapping("/getAllTask/{userId}")
    public ResponseEntity<List<Task>> getTasksByUserId(@PathVariable String userId) {
        // Retrieve and return tasks associated with the given userId
        return ResponseEntity.ok(taskRepository.findByUserId(userId));
    }
    

    // Get a specific task by ID
    @GetMapping("/getTask/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id) {
        // Find task by ID and return it if found, otherwise return 404 Not Found
        return taskRepository.findById(id)
                .map(ResponseEntity::ok) // Return 200 OK with task if found
                .orElse(ResponseEntity.notFound().build()); // Return 404 if task not found
    }

    // Update a task (partial update)
    @PatchMapping("/updateTask/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable String id, @RequestBody Task updatedTask) {
        // Find the task by ID and update only non-null fields
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();

            // Update task fields only if provided in the request body
            if (updatedTask.getTitle() != null) task.setTitle(updatedTask.getTitle());
            if (updatedTask.getDescription() != null) task.setDescription(updatedTask.getDescription());
            if (updatedTask.getPriority() != null) task.setPriority(updatedTask.getPriority());
            if (updatedTask.getCategoryType() != null) task.setCategoryType(updatedTask.getCategoryType());
            if (updatedTask.getDueDate() != null) task.setDueDate(updatedTask.getDueDate());

            task.setCompleted(updatedTask.isCompleted()); // 'false' is a valid value for completion status

            // Save and return the updated task
            return ResponseEntity.ok(taskRepository.save(task));
        } else {
            // Return 404 Not Found if task does not exist
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a task
    @DeleteMapping("/deleteTask/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        // Check if the task exists and delete it, otherwise return 404 Not Found
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            // Return 204 No Content after successful deletion
            return ResponseEntity.noContent().build();
        } else {
            // Return 404 Not Found if task doesn't exist
            return ResponseEntity.notFound().build();
        }
    }
}
