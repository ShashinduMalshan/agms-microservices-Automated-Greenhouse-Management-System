# Automated Greenhouse Management System (AGMS)

The Automated Greenhouse Management System (AGMS) is a cloud-native, microservice-based platform designed to fully automate modern greenhouse environments. The system ingests live telemetry from external IoT sensors, evaluates conditions through a custom rule engine, and triggers automated climate control actions to maintain optimal growing conditions.

---

## Project Overview

Built entirely on the Spring Cloud ecosystem, AGMS replaces manual greenhouse management with a distributed, event-driven infrastructure. The system is organized around clearly bounded microservices, each owning its own data and communicating via well-defined contracts.

### Architectural Highlights

*   **Centralized Configuration**: All microservices fetch environment properties at startup from a Spring Cloud Config Server backed by a local Git repository (config-repo), enabling configuration changes without full rebuilds.
*   **Service Discovery**: Netflix Eureka handles dynamic service registration and resolution. No hardcoded URLs; services locate one another at runtime.
*   **API Gateway and Security**: Spring Cloud Gateway serves as the single entry point. It intercepts all inbound traffic and enforces JWT (JSON Web Token) validation before routing to internal domain services.
*   **Synchronous Inter-Service Communication**: OpenFeign clients enable clean, declarative HTTP communication between services.
*   **External IoT Integration**: Connects to a live Reactive WebFlux IoT Data Provider to register virtual sensors and poll real-time temperature and humidity readings on a scheduled basis.
*   **Polyglot Persistence**: MySQL for structured relational data (Zones, Action Logs) and MongoDB for flexible document-based lifecycle tracking (Crop Inventory).

---

## Technologies and Tools

| Category | Stack |
| :--- | :--- |
| Core Language | Java 17 |
| Application Framework | Spring Boot 4.x |
| Cloud Infrastructure | Netflix Eureka, Spring Cloud Gateway, Spring Cloud Config, OpenFeign |
| Databases | MySQL, MongoDB (Spring Data JPA, Spring Data MongoDB) |
| Security | Spring Security, JWT (HMAC-SHA256) |
| Build and Testing | Maven, Postman |

---

## System Architecture

| Service | Port | Responsibility | Database |
| :--- | :--- | :--- | :--- |
| Config Server | 8888 | Centralized property management via config-repo | None |
| Discovery Server | 8761 | Netflix Eureka service registry | None |
| API Gateway | 8080 | JWT validation and intelligent traffic routing | None |
| Zone Management Service | 8081 | Manages greenhouse sections, thresholds, and IoT device registration | MySQL |
| Sensor Telemetry Service | 8082 | Scheduled worker polling external IoT data every 10 seconds | None |
| Automation & Control Service | 8083 | Rule engine evaluating sensor readings against zone thresholds | MySQL |
| Crop Inventory Service | 8084 | Plant lifecycle state tracking (SEEDLING, VEGETATIVE, HARVESTED) | MongoDB |

---

## Repository Structure

```text
agms-microservices/
├── api-gateway/                  # JWT security filter and route configuration
├── automation-control-service/   # Rule engine and action logging (OpenFeign client)
├── config-repo/                  # Central configuration files (.yml per service)
├── config-server/                # Spring Cloud Config Server
├── crop-inventory-service/       # MongoDB-backed plant lifecycle management
├── discovery-server/             # Netflix Eureka registry
├── docs/                         # Eureka dashboard screenshots and architecture diagrams
├── sensor-telemetry-service/     # External IoT polling and data forwarding
├── zone-management-service/      # Zone CRUD and IoT device registration
├── AGMS_Postman_Collection.json  # Complete exported API test suite
├── pom.xml                       # Parent Maven POM
└── README.md
```

---

## Prerequisites

Ensure the following are installed and running before starting the services:

*   Java 17 JDK or higher
*   Maven
*   MySQL running on localhost:3306
*   MongoDB running on localhost:27017

### Database Initialization

Create the required MySQL schemas before starting any services:

```sql
CREATE DATABASE agms_zone_db;
CREATE DATABASE agms_automation_db;
```

MongoDB will automatically create the agms_crop_db database on the first crop document insertion.

---

## Startup Sequence

This is a distributed system with hard startup dependencies. Services must be started in the order listed below. Wait for each application to report "Started Application" in the console before proceeding to the next.

1.  **Config Server** (Port: 8888)
2.  **Discovery Server** (Port: 8761)
3.  **API Gateway** (Port: 8080)
4.  **Zone Management Service** (Port: 8081)
5.  **Crop Inventory Service** (Port: 8084)
6.  **Automation & Control Service** (Port: 8083)
7.  **Sensor Telemetry Service** (Port: 8082)

The Sensor Telemetry Service must be started last. Upon boot, it immediately begins authenticating with the external IoT API, polling telemetry, and forwarding data to the Automation Service.

### Verification

Open a browser and navigate to http://localhost:8761. All five domain and infrastructure clients should appear with status UP.

---

## Authentication and JWT Security

All domain APIs are protected by the API Gateway's JWT filter. Requests without a valid Bearer Token will be rejected with 401 Unauthorized.

### Step 1: Obtain an Access Token

The /api/auth/login endpoint is exempt from the JWT filter and can be called directly:

```http
POST http://localhost:8080/api/auth/login?username=admin&password=admin
```

### Step 2: Authenticate Subsequent Requests

Attach the token to all subsequent requests using the Authorization header:

```text
Authorization: Bearer <your_token_here>
```

A pre-configured Postman Collection (AGMS_Postman_Collection.json) is included in the root directory.

---

## API Endpoint Reference

All client requests are routed through the API Gateway on port 8080.

### Authentication
*   POST /api/auth/login: Authenticate and receive a JWT access token.

### Zone Management
*   POST /api/zones: Create a zone and register a device.
*   GET /api/zones/{id}: Retrieve zone thresholds and metadata.
*   PUT /api/zones/{id}: Update temperature thresholds.
*   DELETE /api/zones/{id}: Remove a zone.

### Crop Inventory
*   POST /api/crops: Register a new crop batch.
*   GET /api/crops: View current inventory.
*   PUT /api/crops/{id}/status: Update lifecycle stage.

### Sensor Telemetry
*   GET /api/sensors/latest: View the most recent telemetry reading.

### Automation & Control
*   POST /api/automation/process: Internal endpoint for data ingestion.
*   GET /api/automation/logs: View triggered automation actions.

---

## End-to-End Data Flow

1.  **External IoT API** polls data every 10 seconds.
2.  **Sensor Telemetry Service** forwards data to the Automation Service.
3.  **Automation & Control Service** queries the Zone Management Service for thresholds.
4.  **Rule Engine** evaluates the readings and logs actions to the MySQL database.

---

## Documentation and Testing Assets

*   `docs/Eureka dashboard screenshot.png`: Screenshot of registered services.
*   `AGMS_Postman_Collection.json`: Exported Postman collection for API testing.

---

## Author
Shasidu Malshna
