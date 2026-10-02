package com.ridelink.payment.controller;

import com.ridelink.payment.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @PostMapping("/process")
    public ResponseEntity<ApiResponse<Map<String, Object>>> processPayment(@RequestBody Map<String, Object> paymentRequest) {
        // Dynamic mock persistence for payment processing
        Map<String, Object> paymentRecord = Map.of(
                "paymentId", "PAY-" + UUID.randomUUID().toString().substring(0,8),
                "rideId", paymentRequest.get("rideId"),
                "status", "SUCCESS",
                "amount", paymentRequest.get("amount"),
                "receiptNumber", "REC-" + System.currentTimeMillis()
        );
        return ResponseEntity.ok(ApiResponse.success(paymentRecord, "Payment processed successfully!"));
    }
}
