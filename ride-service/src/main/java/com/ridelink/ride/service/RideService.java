package com.ridelink.ride.service;

import com.ridelink.ride.client.DriverServiceClient;
import com.ridelink.ride.client.PaymentServiceClient;
import com.ridelink.ride.dto.ApiResponse;
import com.ridelink.ride.entity.Ride;
import com.ridelink.ride.entity.RideStatus;
import com.ridelink.ride.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private DriverServiceClient driverServiceClient;

    @Autowired
    private PaymentServiceClient paymentServiceClient;

    public Ride requestRide(String passengerId, String pickupLocation, String destination) {
        // Step 1: Initial state is REQUESTED
        Ride ride = new Ride();
        ride.setRideId(UUID.randomUUID().toString());
        ride.setPassengerId(passengerId);
        ride.setPickupLocation(pickupLocation);
        ride.setDestination(destination);
        ride.setStatus(RideStatus.REQUESTED);
        ride = rideRepository.save(ride);

        // Step 2: Make Feign call to Driver Service to find available driver
        ApiResponse<Map<String, Object>> driverResponse = driverServiceClient.getAvailableDriver(pickupLocation);
        
        if (driverResponse.getData() == null || !driverResponse.isSuccess()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No available drivers found in the area");
        }

        String driverId = (String) driverResponse.getData().get("driverId");

        // Step 3: Transition to ASSIGNED
        ride.setDriverId(driverId);
        ride.setStatus(RideStatus.ASSIGNED);
        return rideRepository.save(ride);
    }

    public Ride updateRideStatus(String rideId, RideStatus newStatus, Double amount) {
        Ride ride = rideRepository.findByRideId(rideId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ride not found"));

        validateStateTransition(ride.getStatus(), newStatus);
        
        ride.setStatus(newStatus);

        // Driver Status Synchronization
        if (newStatus == RideStatus.ACCEPTED) {
            driverServiceClient.updateDriverStatus(ride.getDriverId(), "BUSY");
        } else if (newStatus == RideStatus.COMPLETED || newStatus == RideStatus.CANCELLED) {
            driverServiceClient.updateDriverStatus(ride.getDriverId(), "ONLINE");
        }

        // Ride Completion & Payment Processing
        if (newStatus == RideStatus.COMPLETED && amount != null) {
            Map<String, Object> paymentRequest = Map.of(
                    "rideId", ride.getRideId(),
                    "passengerId", ride.getPassengerId(),
                    "amount", amount
            );

            ApiResponse<Map<String, Object>> paymentResponse = paymentServiceClient.processPayment(paymentRequest);
            if (!paymentResponse.isSuccess()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment failed");
            }
            
            ride.setPaymentReference((String) paymentResponse.getData().get("paymentId"));
        }

        return rideRepository.save(ride);
    }

    private void validateStateTransition(RideStatus current, RideStatus next) {
        if (current == RideStatus.COMPLETED || current == RideStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot modify status for an already completed or cancelled ride");
        }
        
        boolean valid = false;
        switch (current) {
            case REQUESTED: valid = (next == RideStatus.ASSIGNED || next == RideStatus.CANCELLED); break;
            case ASSIGNED: valid = (next == RideStatus.ACCEPTED || next == RideStatus.CANCELLED); break;
            case ACCEPTED: valid = (next == RideStatus.IN_PROGRESS || next == RideStatus.CANCELLED); break;
            case IN_PROGRESS: valid = (next == RideStatus.COMPLETED); break;
        }

        if (!valid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "InvalidStateTransitionException: Cannot transition from " + current + " to " + next);
        }
    }
}
