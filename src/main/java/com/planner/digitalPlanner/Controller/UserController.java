package com.planner.digitalPlanner.Controller;

import com.planner.digitalPlanner.Model.User;
import com.planner.digitalPlanner.Repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/users") // Base URL for user-related endpoints
public class UserController {

    @Autowired
    private UserRepository userRepository; // Injecting the UserRepository to interact with MongoDB
    
    // POST create new user
    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody User user, HttpSession session) {
    	//Save user input 
    	String email = user.getEmail();
        
        //Look in saved user repository for entered email to see if the account already exists
        if(userRepository.findByEmail(email) != null) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("An account already exists with that email.");
        }
        
        // Saves a new user to the database
    	userRepository.save(user);
    	
    	session.setAttribute("userName", user.getName());
    	session.setAttribute("userEmail", user.getEmail());
    	
    	return ResponseEntity.ok("Signup successful");
    }
    
    // GET all users
    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        // Fetches and returns all users from the database
        return userRepository.findAll();
    }

    // GET specific user by ID
    @GetMapping("/getUser/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        // Finds a user by ID and returns it if present, otherwise returns 404 Not Found
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT update existing user (using PATCH for partial update)
    @PatchMapping("updateUser/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        // Finds a user by ID and updates the user with the new data if the user exists
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (updatedUser.getName() != null) user.setName(updatedUser.getName());
            if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
            if (updatedUser.getPassword() != null) user.setPassword(updatedUser.getPassword());

            userRepository.save(user);
            return ResponseEntity.ok(user); // Returns updated user
        } else {
            return ResponseEntity.notFound().build(); // If user not found, return 404
        }
    }

    // DELETE a user
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable String id) {
        // Finds a user by ID and deletes the user if found, otherwise returns 404 Not Found
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User deletedUser = optionalUser.get();
            userRepository.deleteById(id);
            return ResponseEntity.ok(deletedUser); // 200 OK with deleted user
        } else {
            return ResponseEntity.notFound().build(); // 404 if user not found
        }
    }
    
    //LOGIN a user
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User loginUser, HttpSession session) {
        //Save user input 
    	String email = loginUser.getEmail();
        String password = loginUser.getPassword();
        
        //Look in saved user repository for entered email and return the user object if found
        User user = userRepository.findByEmail(email);
        
        //If no user email match  -> account doesn't exist
        if(user == null)  {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Account not found. Would you like to sign up?");
        }
        
        //If email matches then check password
        if(!(user.getPassword().equals(password))) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Incorrect email or password.");
        }
        
        session.setAttribute("userEmail", email);

        
        //If both email and login match, login is successful
        return ResponseEntity.ok("Login successful");
    }
    
 // Logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }

    // Update current user endpoint
    @PatchMapping("/updateCurrentUser")
    public ResponseEntity<String> updateCurrentUser(@RequestBody User updatedUser, HttpSession session) {
        //Find current user account
    	String email = (String) session.getAttribute("userEmail");
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (updatedUser.getName() != null && !updatedUser.getName().isEmpty()) user.setName(updatedUser.getName());
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) user.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) user.setPassword(updatedUser.getPassword());

        userRepository.save(user);
        
        // Update session if email was changed
        if (updatedUser.getEmail() != null) {
            session.setAttribute("userEmail", updatedUser.getEmail());
        }
        
        return ResponseEntity.ok("User updated successfully");
    }

    // Delete current user endpoint
    @DeleteMapping("/deleteCurrentUser")
    public ResponseEntity<String> deleteCurrentUser(HttpSession session) {
        String email = (String) session.getAttribute("userEmail");
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        userRepository.delete(user);
        session.invalidate();
        return ResponseEntity.ok("User deleted successfully");
    }
    
    @GetMapping("/checkSession")
    public ResponseEntity<Map<String, Object>> checkSession(HttpSession session) {
    	//Retrieve session-saved email
    	String email = (String) session.getAttribute("userEmail");
    	
    	//In case user first logged in on a new session, find associated name from database
    	String name = null;
    	if(email != null) {
    		User user = userRepository.findByEmail(email);
    		if(user != null) {
    			name = user.getName();
    		}
    	}
    	
    	//Return logged in, email, and name JSON information
    	Map<String, Object> result = new HashMap<>();
    	result.put("loggedIn", email != null);
    	if(email != null) {
    		result.put("email", email);
    		result.put("name", name);
    	}
    	
    	return ResponseEntity.ok(result);
    }
    
    @GetMapping("/sessionUser")
    public ResponseEntity<User> getUserFromSession(HttpSession session) {
        String email = (String) session.getAttribute("userEmail");

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }
}
