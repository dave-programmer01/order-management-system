# Order Management System

A microservices-based Order Management System built with Spring Boot, Spring Cloud, and Docker.

## Microservices Architecture

The system consists of several microservices:

*   **Discovery Server**: Eureka server for service registration and discovery.
*   **API Gateway**: Central entry point for all client requests, providing routing and security.
*   **Auth Service**: Handles user registration, authentication, and JWT generation.
*   **Product Service**: Manages product catalog and inventory.
*   **Order Service**: Processes customer orders and interacts with the Product Service.

## Prerequisites

*   JDK 21
*   Maven
*   Docker and Docker Compose (optional)

## Building the Project

To build all microservices, run the following command from the root directory:

```bash
mvn clean package -DskipTests
```

## Running the Services

You can run each service individually or use Docker.

### Using Docker

Each service contains a `Dockerfile`. To containerize the services, you can build images for each:

```bash
# Example for Order Service
cd order-service
docker build -t order-service .
```

## Project Structure

```text
.
├── api-gateway/
├── auth-service/
├── discovery-server/
├── order-service/
├── product-service/
└── pom.xml
```

## Technologies Used

*   Spring Boot 3
*   Spring Cloud (Eureka, Gateway)
*   Spring Data JPA
*   Spring Security & JWT
*   Lombok
*   Resilience4j (Circuit Breaker, Retry)
*   PostgreSQL / H2
