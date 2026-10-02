package com.ridelink.ride.repository;

import com.ridelink.ride.entity.Ride;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RideRepository extends MongoRepository<Ride, String> {
    Optional<Ride> findByRideId(String rideId);
}
