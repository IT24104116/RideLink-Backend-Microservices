package com.ridelink.account.controller;

import com.ridelink.account.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<String, String>>> register() {
        // Dummy implementation for now so you can test it in Postman
        Map<String, String> dummyData = Map.of(
                "userId", "USER-123",
                "role", "ROLE_PASSENGER"
        );
        return ResponseEntity.ok(ApiResponse.success(dummyData, "User registered successfully"));
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login() {
        // Dummy implementation for now so you can test it in Postman
        Map<String, String> dummyData = Map.of(
                "token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        );
        return ResponseEntity.ok(ApiResponse.success(dummyData, "Login successful"));
    }
}
