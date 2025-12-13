package com.riegelnick.backend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * JWT Utility class for creating and validating JWT tokens.
 * 
 * How JWT works:
 * 1. User logs in → Server creates JWT token with user info
 * 2. Token is sent to client (stored in localStorage)
 * 3. Client sends token with each request in Authorization header
 * 4. Server validates token to authenticate user
 * 
 * JWT Structure: header.payload.signature
 * - Header: token type and algorithm (HS256)
 * - Payload: user data (username, expiration, etc.)
 * - Signature: ensures token hasn't been tampered with
 */
@Component
public class JwtUtil {

    // Secret key used to sign tokens - ensures tokens can't be forged
    // In production, store this in environment variables or application.properties
    // This key is randomly generated each time the app starts
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // How long tokens are valid before they expire
    // 1000 = 1 second (in milliseconds)
    // 1000 * 60 = 1 minute
    // 1000 * 60 * 60 = 1 hour
    // 1000 * 60 * 60 * 10 = 10 hours
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    /**
     * Generate a JWT token for a user.
     * 
     * @param username The user's email/username to include in the token
     * @return A signed JWT token string
     * 
     * Example: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjQwMTIzNDU2LCJleHAiOjE2NDAxNTk0NTZ9.signature"
     */
    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)  // "sub" claim: who the token is for
                .setIssuedAt(new Date(System.currentTimeMillis()))  // "iat" claim: when token was created
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // "exp" claim: when token expires
                .signWith(SECRET_KEY)  // Sign with secret key to prevent tampering
                .compact();  // Build the final JWT string
    }

    /**
     * Validate a JWT token.
     * Checks if:
     * 1. The username in the token matches the expected username
     * 2. The token hasn't expired
     * 
     * @param token The JWT token to validate
     * @param username The expected username to match against
     * @return true if token is valid, false otherwise
     */
    public Boolean validateToken(String token, String username){
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * Extract the username (subject) from a JWT token.
     * The username is stored in the "sub" (subject) claim of the token.
     * 
     * @param token The JWT token
     * @return The username/email from the token
     */
    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    /**
     * Parse a JWT token and extract all claims (data) from it.
     * Claims are the pieces of information stored in the token payload.
     * 
     * @param token The JWT token to parse
     * @return Claims object containing all data from the token
     * @throws io.jsonwebtoken.JwtException if token is invalid or tampered with
     */
    private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)  // Use same key that was used to sign the token
            .build()
            .parseClaimsJws(token)  // Parse and verify the token
            .getBody();  // Get the claims (payload)
    }

    /**
     * Check if a token has expired.
     * Compares the token's expiration time to the current time.
     * 
     * @param token The JWT token to check
     * @return true if token is expired, false if still valid
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());  // Is expiration date before now?
    }
}
