package com.riegelnick.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.riegelnick.backend.entities.User;
import com.riegelnick.backend.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    // Register new user
    public User registerUser(User user) {

        // Validate user input
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("User data cannot be null");
        }
        
        // Normalize email
        String email = user.getEmail().trim().toLowerCase();
        user.setEmail(email);
        
        // Validate email input
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Validate password input
        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        // Check if email exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        // Hash password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user
        return userRepository.save(user);
    }
    
    // Spring Security requires this method
    @Override
    public UserDetails loadUserByUsername(String email) {
        
        // Find user by email
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
            // Return UserDetails object
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getEmail())
            .password(user.getPassword())
            .roles(user.getRole())
            .build();
    }
}