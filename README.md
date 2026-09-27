# RideLink Microservices Architecture

This repository contains 4 decoupled microservices built with Spring Boot, communicating via OpenFeign with separate MongoDB databases.

## Services
1. **Account Service** (Port: 8081, DB: `account_db`)
2. **Driver Service** (Port: 8082, DB: `driver_db`)
3. **Ride Service** (Port: 8083, DB: `ride_db`)
4. **Payment Service** (Port: 8084, DB: `payment_db`)

## Execution
Run each service using Maven:
```bash
mvn spring-boot:run -pl account-service
mvn spring-boot:run -pl driver-service
mvn spring-boot:run -pl ride-service
mvn spring-boot:run -pl payment-service
```
