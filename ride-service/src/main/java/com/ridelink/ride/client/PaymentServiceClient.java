package com.ridelink.ride.client;

import com.ridelink.ride.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "payment-service", url = "http://localhost:8084")
public interface PaymentServiceClient {

    @PostMapping("/api/v1/payments/process")
    ApiResponse<Map<String, Object>> processPayment(@RequestBody Map<String, Object> paymentRequest);
}
