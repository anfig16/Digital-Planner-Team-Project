package com.planner.digitalPlanner.Controller;

import com.planner.digitalPlanner.Model.Category;
import com.planner.digitalPlanner.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories") // Base URL for category-related endpoints
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository; // Injecting CategoryRepository to interact with the database

    // Create a new category
    @PostMapping("/createCategory")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        // Save the new category and return it with status 200 OK
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    // Get all categories by userId
    @GetMapping("/getCategories/{userId}")
    public List<Category> getCategoriesByUserId(@PathVariable String userId) {
        // Retrieve and return all categories associated with the given userId
        return categoryRepository.findByUserId(userId);
    }

    // Get a specific category by ID
    @GetMapping("/getCategory/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable String id) {
        // Find the category by ID and return it if found, otherwise return 404 Not Found
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok) // Return 200 OK with the category if found
                .orElse(ResponseEntity.notFound().build()); // Return 404 if category not found
    }

    // Update a category (partial update)
    @PatchMapping("/updateCategory/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable String id, @RequestBody Category updatedCategory) {
        // Find the category by ID and update non-null fields
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();

            // Update the category fields only if provided in the request body
            if (updatedCategory.getLabel() != null)
                category.setLabel(updatedCategory.getLabel());

            if (updatedCategory.getColorHex() != null)
                category.setColorHex(updatedCategory.getColorHex());

            if (updatedCategory.getUserId() != null)
                category.setUserId(updatedCategory.getUserId());

            // Save and return the updated category
            return ResponseEntity.ok(categoryRepository.save(category));
        } else {
            // Return 404 Not Found if category does not exist
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a category
    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        // Check if the category exists and delete it, otherwise return 404 Not Found
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            // Return 204 No Content after successful deletion
            return ResponseEntity.noContent().build();
        } else {
            // Return 404 Not Found if category doesn't exist
            return ResponseEntity.notFound().build();
        }
    }
}
