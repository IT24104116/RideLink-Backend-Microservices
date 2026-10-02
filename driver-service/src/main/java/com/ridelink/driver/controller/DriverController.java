package com.ridelink.driver.controller;

import com.ridelink.driver.dto.ApiResponse;
import com.ridelink.driver.entity.Driver;
import com.ridelink.driver.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Driver>> createDriver(@RequestBody Driver driver) {
        Driver savedDriver = driverService.createDriver(driver);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(savedDriver, "Driver created successfully"));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAvailableDriver(@RequestParam("location") String location) {
        // Dynamic mock persistence for available driver
        Map<String, Object> driverData = Map.of(
                "driverId", "DRIVER-" + UUID.randomUUID().toString().substring(0,8),
                "status", "AVAILABLE",
                "location", location
        );
        return ResponseEntity.ok(ApiResponse.success(driverData, "Available driver found"));
    }

    @PutMapping("/{driverId}/status")
    public ResponseEntity<ApiResponse<Void>> updateDriverStatus(@PathVariable String driverId, @RequestParam("status") String status) {
        // Logic to update driver status in DB goes here
        return ResponseEntity.ok(ApiResponse.success(null, "Driver status updated to " + status));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal Server Error: " + ex.getMessage()));
    }
}

