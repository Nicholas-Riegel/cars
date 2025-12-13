package com.riegelnick.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.riegelnick.backend.entities.User;
import com.riegelnick.backend.services.UserService;
import com.riegelnick.backend.utils.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        
        try {

            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        try {

            // Load user from database by email
            // This returns a UserDetails object (not our User entity)
            UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
        
            // Check if the password matches
            // Compare plain text password (from request) with hashed password (from database)
            boolean passwordMatches = passwordEncoder.matches(
                loginRequest.getPassword(),           // Plain text password from user
                userDetails.getPassword()        // Hashed password from database
            );

            // If password doesn't match, return error
            if (!passwordMatches) {
                return ResponseEntity.status(401).body("Invalid credentials");
            }

             // Password is correct. Generate JWT token
            String token = jwtUtil.generateToken(loginRequest.getEmail());
        
            // Return the token to the client
            return ResponseEntity.ok(token);
        
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Login failed: " + e.getMessage());
        }
    }
    
    // Inner class for login request payload
    public static class LoginRequest {

        private String email;
        private String password;
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }
}
