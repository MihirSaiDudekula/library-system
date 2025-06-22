package com.example.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FallbackController {
    
    @GetMapping("/fallback/book-service")
    public ResponseEntity<Map<String, String>> bookServiceFallback() {
        return createFallbackResponse("Book service is currently unavailable. Please try again later.");
    }
    
    @GetMapping("/fallback/user-service")
    public ResponseEntity<Map<String, String>> userServiceFallback() {
        return createFallbackResponse("User service is currently unavailable. Please try again later.");
    }
    
    @GetMapping("/fallback/borrowing-service")
    public ResponseEntity<Map<String, String>> borrowingServiceFallback() {
        return createFallbackResponse("Borrowing service is currently unavailable. Please try again later.");
    }
    
    private ResponseEntity<Map<String, String>> createFallbackResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        response.put("status", "SERVICE_UNAVAILABLE");
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
