# Java Inventory Microservices

A modern microservices architecture for event ticket booking and inventory management built using Spring Boot, Spring Cloud, Kafka, PostgreSQL, and Keycloak.

## Project Overview

This project implements a distributed system for managing event tickets with the following components:

- **API Gateway**: Entry point for all client requests
- **Inventory Service**: Manages event information and ticket availability
- **Booking Service**: Handles ticket booking requests and initiates order processing
- **Order Service**: Processes and finalizes orders received from the booking service

## Architecture

```
                        ┌─────────────────┐
                        │                 │
                        │   API Gateway   │
                        │   (Port 8090)   │
                        │                 │
                        └─────────────────┘
                                │
                                ▼
           ┌────────────────────┬────────────────────┐
           │                    │                    │
           ▼                    ▼                    ▼
┌─────────────────┐   ┌─────────────────┐   ┌─────────────────┐
│                 │   │                 │   │                 │
│Inventory Service│   │ Booking Service │   │  Order Service  │
│   (Port 8080)   │   │   (Port 8081)   │───│   (Port 8082)   │
│                 │   │                 │   │                 │
└────────┬────────┘   └────────┬────────┘   └────────┬────────┘
         │                     │                     │
         │                     │                     │
         └──────────┐ ┌────────┘                     │
                    ▼ ▼                              ▼
          ┌─────────────────────┐          ┌─────────────────┐
          │                     │          │                 │
          │  PostgreSQL (5432)  │          │ Kafka (9092)    │
          │                     │          │                 │
          └─────────────────────┘          └─────────────────┘
                                                    │
                                                    │
                                                    ▼
                                         ┌─────────────────────┐
                                         │                     │
                                         │  Keycloak (8091)    │
                                         │                     │
                                         └─────────────────────┘
```

## Technologies Used

### Spring Boot & Spring Cloud (v3.5.3)
- **Spring Boot**: For building stand-alone, production-grade Spring-based applications
- **Spring Data JPA**: For data access layer
- **Spring Cloud Gateway**: For API gateway implementation
- **Spring Kafka**: For messaging between services

### Databases & Persistence
- **PostgreSQL**: Primary database for all services
- **Flyway**: For database migrations in Inventory Service

### Monitoring & Resilience
- **Spring Actuator**: For monitoring and management endpoints
- **Resilience4j**: For circuit breaking and fault tolerance

### Security
- **Keycloak**: For OAuth2/OpenID Connect authentication and authorization
- **Spring Security OAuth2**: For securing APIs

### Documentation
- **SpringDoc OpenAPI**: For API documentation via Swagger UI

### Other Tools
- **Lombok**: For reducing boilerplate code
- **Maven**: For dependency management and build automation
- **Docker**: For containerization (includes Keycloak and PostgreSQL setup)

## Service Descriptions

### API Gateway (Port: 8090)
- Entry point for all client requests
- Routes requests to appropriate microservices
- Handles authentication and authorization via Keycloak
- Provides unified Swagger UI documentation for all services
- Implements circuit breakers with Resilience4j

### Inventory Service (Port: 8080)
- Manages event information
- Tracks ticket inventory and availability
- Handles event creation, updates, and deletions
- Uses Flyway for database migrations

### Booking Service (Port: 8081)
- Processes ticket booking requests
- Validates inventory availability
- Publishes booking events to Kafka
- Integrates with Inventory Service to update ticket availability

### Order Service (Port: 8082)
- Consumes booking events from Kafka
- Creates and manages customer orders
- Processes payment information
- Maintains order history

## Setup and Installation

### Prerequisites
- Java 21
- Maven
- Docker and Docker Compose
- PostgreSQL
- Kafka

### Database Setup
1. Ensure PostgreSQL is running on port 5432
2. Create a database named "ticketing"
3. Use the following credentials:
   - Username: postgres
   - Password: password

### Kafka Setup
- Ensure Kafka is running on port 9092

### Keycloak Setup
1. Run Keycloak using the Docker Compose file in the inventoryservice directory
2. Import the realm configuration from inventoryservice/docker/keycloak/realms

### Starting the Services
1. Start each service in the following order:
   - Inventory Service
   - Booking Service
   - Order Service
   - API Gateway

## API Documentation

Access the Swagger UI documentation for all services via:
- http://localhost:8090/swagger-ui.html

Individual service documentation:
- Inventory Service: http://localhost:8080/swagger-ui.html
- Booking Service: http://localhost:8081/swagger-ui.html
