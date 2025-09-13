package com.planner.digitalPlanner.Controller;

import com.planner.digitalPlanner.Model.Themes;
import com.planner.digitalPlanner.Repository.ThemesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/themes") // Base URL for theme-related endpoints
public class ThemesController {

    @Autowired
    private ThemesRepository themeRepository; // Injecting the ThemesRepository to interact with MongoDB

    // Create a new theme
    @PostMapping("/createTheme")
    public Themes createTheme(@RequestBody Themes theme) {
        // Saves a new theme and returns the saved theme object
        return themeRepository.save(theme);
    }

    // Get all themes (optionally filtered by userId or isDefault)
    @GetMapping("/getThemes")
    public List<Themes> getAllThemes(@RequestParam(required = false) String userId,
                                      @RequestParam(required = false) Boolean isDefault) {
        // If userId is provided, return themes specific to that user
        if (userId != null) {
            return themeRepository.findByUserId(userId);
        // If isDefault is true, return only default themes
        } else if (isDefault != null && isDefault) {
            return themeRepository.findByIsDefaultTrue();
        // Otherwise, return all themes
        } else {
            return themeRepository.findAll();
        }
    }
    
    // Get themes of a user by userID
    @GetMapping("/getThemes/{userId}")
    public ResponseEntity<List<Themes>> getThemesByUserId(@PathVariable String userId) {
        // Retrieves both user-specific themes and default themes
        List<Themes> userThemes = themeRepository.findByUserId(userId);
        List<Themes> defaultThemes = themeRepository.findByIsDefaultTrue();

        userThemes.addAll(defaultThemes); // Combine both lists
        return ResponseEntity.ok(userThemes);
    }

    // Get theme by ID
    @GetMapping("/getTheme/{id}")
    public ResponseEntity<Themes> getThemeById(@PathVariable String id) {
        // Find theme by ID and return it if present, otherwise return 404 Not Found
        return themeRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    // Patch update a theme (works like PUT but does not require passing null values)
    @PatchMapping("updateTheme/{id}")
    public ResponseEntity<Themes> partialUpdateTheme(@PathVariable String id, @RequestBody Themes updates) {
        // Find the existing theme by ID and update with new values if provided
        Optional<Themes> optionalTheme = themeRepository.findById(id);

        if (optionalTheme.isPresent()) {
            Themes existing = optionalTheme.get();

            // Only update non-null fields
            if (updates.getName() != null) {
                existing.setName(updates.getName());
            }
            if (updates.getPrimaryColor() != null) {
                existing.setPrimaryColor(updates.getPrimaryColor());
            }
            if (updates.getSecondaryColor() != null) {
                existing.setSecondaryColor(updates.getSecondaryColor());
            }
            if (updates.getBackgroundImageUrl() != null) {
                existing.setBackgroundImageUrl(updates.getBackgroundImageUrl());
            }
            if (updates.getTextColor() != null) {
                existing.setTextColor(updates.getTextColor());
            }

            // Optionally update isDefault if explicitly provided
            existing.setDefault(updates.isDefault());

            return ResponseEntity.ok(themeRepository.save(existing)); // Save and return updated theme
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if theme not found
        }
    }

    // Delete a theme by ID
    @DeleteMapping("deleteTheme/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable String id) {
        // Check if theme exists, then delete it; otherwise return 404 Not Found
        if (themeRepository.existsById(id)) {
            themeRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // Return 204 No Content after successful deletion
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if theme not found
        }
    }
}
