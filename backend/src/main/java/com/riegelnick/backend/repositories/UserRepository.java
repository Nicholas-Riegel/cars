package com.riegelnick.backend.repositories;

import com.riegelnick.backend.entities.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // SPRING DATA JPA MAGIC:
    // You only declare method signatures - Spring automatically generates the implementation at runtime!
    // It parses the method name to understand what query to create.
    //
    // Naming convention:
    // - findBy + FieldName → SELECT * FROM table WHERE field = ?
    // - existsBy + FieldName → SELECT COUNT(*) FROM table WHERE field = ? > 0
    // - deleteBy + FieldName → DELETE FROM table WHERE field = ?
    //
    // Examples:
    // Optional<User> findByEmail(String email) → SELECT * FROM users WHERE email = ?
    // List<User> findByRole(String role) → SELECT * FROM users WHERE role = ?
    // boolean existsByEmail(String email) → SELECT COUNT(*) > 0 FROM users WHERE email = ?

    // Find user by email - needed for login/authentication
    Optional<User> findByEmail(String email);
    
    // Check if email already exists - useful for registration
    boolean existsByEmail(String email);
}