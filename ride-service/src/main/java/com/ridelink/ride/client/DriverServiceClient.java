package com.ridelink.ride.client;

import com.ridelink.ride.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "driver-service", url = "http://localhost:8082")
public interface DriverServiceClient {

    @GetMapping("/api/v1/drivers/available")
    ApiResponse<Map<String, Object>> getAvailableDriver(@RequestParam("location") String location);

    @PutMapping("/api/v1/drivers/{driverId}/status")
    ApiResponse<Void> updateDriverStatus(@PathVariable("driverId") String driverId, @RequestParam("status") String status);
}
