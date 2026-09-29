package com.ridelink.ride.controller;

import com.ridelink.ride.dto.ApiResponse;
import com.ridelink.ride.entity.Ride;
import com.ridelink.ride.entity.RideStatus;
import com.ridelink.ride.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/rides")
public class RideController {

    @Autowired
    private RideService rideService;

    @PostMapping("/request")
    public ResponseEntity<ApiResponse<Ride>> requestRide(@RequestBody Map<String, String> request) {
        String pickup = request.getOrDefault("pickupLocation", request.get("pickup"));
        String destination = request.getOrDefault("dropoffLocation", request.get("destination"));
        Ride ride = rideService.requestRide(request.get("passengerId"), pickup, destination);
        return ResponseEntity.ok(ApiResponse.success(ride, "Ride requested and driver assigned successfully."));
    }

    @PutMapping("/{rideId}/status")
    public ResponseEntity<ApiResponse<Ride>> updateRideStatus(@PathVariable String rideId, @RequestBody Map<String, Object> request) {
        RideStatus newStatus = RideStatus.valueOf((String) request.get("status"));
        Double amount = request.containsKey("amount") ? Double.valueOf(request.get("amount").toString()) : null;
        
        Ride ride = rideService.updateRideStatus(rideId, newStatus, amount);
        return ResponseEntity.ok(ApiResponse.success(ride, "Ride status updated successfully."));
    }
}
