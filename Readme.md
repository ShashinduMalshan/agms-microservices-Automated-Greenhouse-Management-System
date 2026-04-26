# Automated Greenhouse Management System (AGMS)

The Automated Greenhouse Management System (AGMS) is a cloud-native, microservice-based platform designed to fully automate modern greenhouse environments. The system ingests live telemetry from external IoT sensors, evaluates conditions through a custom rule engine, and triggers automated climate control actions to maintain optimal growing conditions.

---

## Project Overview

Built on the Spring Cloud ecosystem, AGMS provides a distributed, event-driven infrastructure for greenhouse management. The system is organized into microservices, each owning its data and communicating through well-defined contracts.

### Architectural Highlights

*   **Centralized Configuration**: Microservices fetch environment properties from a Spring Cloud Config Server backed by a local Git repository (config-repo).
*   **Service Discovery**: Netflix Eureka handles dynamic service registration and resolution.
*   **API Gateway and Security**: Spring Cloud Gateway serves as the single entry point, enforcing JWT (JSON Web Token) validation.
*   **Inter-Service Communication**: OpenFeign clients enable declarative HTTP communication between services.
*   **IoT Integration**: Connects to a Reactive WebFlux IoT Data Provider to poll real-time sensor data.
*   **Polyglot Persistence**: Uses MySQL for relational data and MongoDB for flexible document-based lifecycle tracking.

---

## Technologies and Tools

| Category | Stack |
| :--- | :--- |
| Core Language | Java 17 |
| Application Framework | Spring Boot 4.x |
| Cloud Infrastructure | Netflix Eureka, Spring Cloud Gateway, Spring Cloud Config, OpenFeign |
| Databases | MySQL, MongoDB |
| Security | Spring Security, JWT (HMAC-SHA256) |
| Build and Testing | Maven, Postman |

---

## System Architecture

| Service | Port | Responsibility | Database |
| :--- | :--- | :--- | :--- |
| Config Server | 8888 | Centralized property management | None |
| Discovery Server | 8761 | Service registry | None |
| API Gateway | 8080 | JWT validation and routing | None |
| Zone Management Service | 8081 | Greenhouse sections and device registration | MySQL |
| Sensor Telemetry Service | 8082 | Polling external IoT data | None |
| Automation & Control Service | 8083 | Rule engine evaluating sensor readings | MySQL |
| Crop Inventory Service | 8084 | Plant lifecycle tracking | MongoDB |

---

## Repository Structure

```text
agms-microservices/
├── api-gateway/                  # Security filter and route configuration
├── automation-control-service/   # Rule engine and action logging
├── config-repo/                  # Central configuration files
├── config-server/                # Spring Cloud Config Server
├── crop-inventory-service/       # Plant lifecycle management
├── discovery-server/             # Eureka registry
├── docs/                         # Screenshots and diagrams
├── sensor-telemetry-service/     # IoT polling and data forwarding
├── zone-management-service/      # Zone CRUD and device registration
├── AGMS_Postman_Collection.json  # API test suite
├── pom.xml                       # Parent Maven POM
└── README.md
```

---

## Prerequisites

Ensure the following are installed and running:

*   Java 17 JDK or higher
*   Maven
*   MySQL running on localhost:3306
*   MongoDB running on localhost:27017

### Database Initialization

Create the MySQL schemas:

```sql
CREATE DATABASE agms_zone_db;
CREATE DATABASE agms_automation_db;
```

MongoDB will create `agms_crop_db` on the first insertion.

---

## Startup Sequence

Services must be started in the order listed below. Wait for each application to report "Started Application" before proceeding.

1.  Config Server (Port: 8888)
2.  Discovery Server (Port: 8761)
3.  API Gateway (Port: 8080)
4.  Zone Management Service (Port: 8081)
5.  Crop Inventory Service (Port: 8084)
6.  Automation & Control Service (Port: 8083)
7.  Sensor Telemetry Service (Port: 8082)

### Verification

Navigate to http://localhost:8761 in your browser. All services should appear with status UP.

---

## Authentication and Security

All domain APIs are protected by the API Gateway's JWT filter.

### 1. Obtain an Access Token

Call the login endpoint:

```http
POST http://localhost:8080/api/auth/login?username=admin&password=admin
```

### 2. Authenticate Requests

Attach the token to the Authorization header:

```text
Authorization: Bearer <your_token_here>
```

A Postman Collection is available in the root directory for testing.

---

## API Endpoint Reference

### Authentication
*   POST /api/auth/login: Get JWT access token.

### Zone Management
*   POST /api/zones: Create a zone.
*   GET /api/zones/{id}: Retrieve zone metadata.
*   PUT /api/zones/{id}: Update thresholds.
*   DELETE /api/zones/{id}: Remove a zone.

### Crop Inventory
*   POST /api/crops: Register a crop batch.
*   GET /api/crops: View inventory.
*   PUT /api/crops/{id}/status: Update status.

### Sensor Telemetry
*   GET /api/sensors/latest: View recent telemetry.

### Automation & Control
*   POST /api/automation/process: Data ingestion.
*   GET /api/automation/logs: View action logs.

---

## End-to-End Data Flow

1.  External IoT API polls data every 10 seconds.
2.  Sensor Telemetry Service forwards data.
3.  Automation & Control Service checks thresholds.
4.  Rule Engine evaluates readings and logs actions.

---

## Documentation

*   `docs/Eureka dashboard screenshot.png`: Screenshot of registered services.
*   `AGMS_Postman_Collection.json`: API testing collection.

---

## Author
Shasidu Malshan
