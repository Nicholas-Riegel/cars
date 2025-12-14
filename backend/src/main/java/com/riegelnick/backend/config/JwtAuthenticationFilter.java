package com.riegelnick.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.riegelnick.backend.services.UserService;
import com.riegelnick.backend.utils.JwtUtil;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT Authentication Filter - This is the gatekeeper for protected endpoints!
 * 
 * HOW IT WORKS:
 * This filter runs BEFORE every HTTP request reaches your controllers.
 * It checks if the request has a valid JWT token.
 * If valid, it tells Spring Security "this user is authenticated, let them through"
 * If invalid/missing, Spring Security will block access to protected endpoints.
 * 
 * FLOW:
 * 1. Browser sends request with: Authorization: Bearer <jwt-token>
 * 2. This filter intercepts the request
 * 3. Extracts and validates the token
 * 4. If valid, sets authentication in Spring Security context
 * 5. Request continues to your controller
 * 
 * OncePerRequestFilter = ensures this filter only runs once per request
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    /**
     * Skip JWT validation for public endpoints like login and setup-admin.
     * These endpoints don't need authentication.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/auth/login") 
        // || path.equals("/api/auth/setup-admin")
        ;
    }

    /**
     * This method is called for EVERY HTTP request to your API.
     * It's our chance to check for a JWT token and authenticate the user.
     * 
     * @param request - The incoming HTTP request (contains headers, body, etc.)
     * @param response - The HTTP response we'll send back
     * @param filterChain - The chain of filters/handlers for this request
     */
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        // STEP 1: Look for the Authorization header in the request
        // Example header: "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
        // This is where the frontend sends the JWT token
        String authHeader = request.getHeader("Authorization");
        
        String token = null;      // Will hold the JWT token (if found)
        String username = null;   // Will hold the username extracted from token

        // STEP 2: Check if Authorization header exists and follows correct format
        // JWT tokens should be sent as: "Bearer <token>"
        // The word "Bearer" indicates it's a bearer token authentication scheme
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            
            // STEP 3: Extract the actual token
            // substring(7) removes "Bearer " (which is 7 characters including the space)
            // Example: "Bearer abc123" → "abc123"
            token = authHeader.substring(7);
            
            // STEP 4: Try to extract the username from the token
            // The token contains encoded data including the username
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                // If extraction fails, token is malformed or invalid
                // We just log it and continue (user won't be authenticated)
                System.out.println("Invalid token: " + e.getMessage());
            }
        }
        
        // STEP 5: If we successfully extracted a username AND user isn't already authenticated
        // SecurityContextHolder.getContext().getAuthentication() = Spring's way to check if someone is logged in
        // If it's null, no one is currently authenticated for this request
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // STEP 6: Load the full user details from our database
            // We need this to get the user's roles/authorities
            UserDetails userDetails = userService.loadUserByUsername(username);
            
            // STEP 7: Validate the token is legitimate
            // Checks: Does the username match? Is the token expired?
            if (jwtUtil.validateToken(token, username)) {
                
                // STEP 8: Token is valid! Create an authentication object
                // This is Spring Security's way of saying "this user is authenticated"
                // Parameters:
                // - userDetails: who is authenticated
                // - null: credentials (we don't need to pass password here)
                // - userDetails.getAuthorities(): user's roles (USER, ADMIN, etc.)
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                
                // STEP 9: Add request details to the authentication object
                // This includes IP address, session ID, etc. (useful for security logging)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // STEP 10: Tell Spring Security "this user is authenticated!"
                // Sets the authentication in the security context for this request
                // Now Spring Security will allow access to protected endpoints
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // STEP 11: Continue the request to the next filter/controller
        // This MUST be called, or the request will hang
        // Whether authentication succeeded or failed, we let the request continue
        // Spring Security will decide if they can access the endpoint based on the authentication we set (or didn't set)
        filterChain.doFilter(request, response);
    }
}
