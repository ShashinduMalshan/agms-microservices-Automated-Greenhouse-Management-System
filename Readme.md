# 🌿 Automated Greenhouse Management System (AGMS)

![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen.svg)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-Microservices-blue.svg)
![MySQL](https://img.shields.io/badge/MySQL-Relational-lightgrey.svg)
![MongoDB](https://img.shields.io/badge/MongoDB-NoSQL-green.svg)

A cloud-native, microservice-based platform designed to fully automate modern greenhouse environments. AGMS ingests live telemetry from external IoT sensors, evaluates conditions through a custom rule engine, and triggers automated climate control actions to maintain optimal growing conditions.

---

## Project Overview

Built entirely on the **Spring Cloud ecosystem**, AGMS replaces error-prone manual greenhouse management with a distributed, event-driven infrastructure. The system is organized around clearly bounded microservices, each owning its own data and communicating via well-defined contracts.

**Architectural Highlights**

- **Centralized Configuration** — All microservices fetch environment properties at startup from a Spring Cloud Config Server backed by a local Git repository (`config-repo`), enabling configuration changes without full rebuilds.
- **Service Discovery** — Netflix Eureka handles dynamic service registration and resolution. No hardcoded URLs; services locate one another at runtime.
- **API Gateway and Security** — Spring Cloud Gateway serves as the single entry point. It intercepts all inbound traffic and enforces JWT (JSON Web Token) validation before routing to internal domain services.
- **Synchronous Inter-Service Communication** — OpenFeign clients enable clean, declarative HTTP communication between services (e.g., Automation Service querying Zone Service for temperature thresholds).
- **External IoT Integration** — Connects to a live Reactive WebFlux IoT Data Provider to register virtual sensors and poll real-time temperature and humidity readings on a scheduled basis.
- **Polyglot Persistence** — MySQL for structured relational data (Zones, Action Logs) and MongoDB for flexible document-based lifecycle tracking (Crop Inventory).

---

## Technologies and Tools

| Category | Stack                                                                |
| :--- |:---------------------------------------------------------------------|
| Core Language | Java 17                                                              |
| Application Framework | Spring Boot 4.x                                                      |
| Cloud Infrastructure | Netflix Eureka, Spring Cloud Gateway, Spring Cloud Config, OpenFeign |
| Databases | MySQL, MongoDB (Spring Data JPA, Spring Data MongoDB)                |
| Security | Spring Security, JWT (HMAC-SHA256)                                   |
| Build and Testing | Maven, Postman                                                       |

---

## System Architecture

| Service | Port | Responsibility | Database |
| :--- | :--- | :--- | :--- |
| Config Server | `8888` | Centralized property management via `config-repo` | — |
| Discovery Server | `8761` | Netflix Eureka service registry | — |
| API Gateway | `8080` | JWT validation and intelligent traffic routing | — |
| Zone Management Service | `8081` | Manages greenhouse sections, thresholds, and IoT device registration | MySQL |
| Sensor Telemetry Service | `8082` | Scheduled worker polling external IoT data every 10 seconds | — |
| Automation & Control Service | `8083` | Rule engine evaluating sensor readings against zone thresholds | MySQL |
| Crop Inventory Service | `8084` | Plant lifecycle state tracking (`SEEDLING → VEGETATIVE → HARVESTED`) | MongoDB |

---

## Repository Structure

```
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

- **Java 17 JDK** or higher
- **Maven** (or use the included `./mvnw` wrapper)
- **MySQL** running on `localhost:3306`
- **MongoDB** running on `localhost:27017`

### Database Initialization

Create the required MySQL schemas before starting any services:

```sql
CREATE DATABASE agms_zone_db;
CREATE DATABASE agms_automation_db;
```

MongoDB will automatically create the `agms_crop_db` database on the first crop document insertion.

---

## Startup Sequence

This is a distributed system with hard startup dependencies. Services **must be started in the order listed below**. Wait for each application to report `Started Application in X seconds` in the console before proceeding to the next.

| Order | Service | Main Class | Port |
| :--- | :--- | :--- | :--- |
| 1 | Config Server | `ConfigServerApplication` | `8888` |
| 2 | Discovery Server | `DiscoveryServerApplication` | `8761` |
| 3 | API Gateway | `ApiGatewayApplication` | `8080` |
| 4 | Zone Management Service | `ZoneManagementServiceApplication` | `8081` |
| 5 | Crop Inventory Service | `CropInventoryServiceApplication` | `8084` |
| 6 | Automation & Control Service | `AutomationControlServiceApplication` | `8083` |
| 7 | Sensor Telemetry Service | `SensorTelemetryServiceApplication` | `8082` |

The Sensor Telemetry Service must be started last. Upon boot, it immediately begins authenticating with the external IoT API, polling telemetry, and forwarding data to the Automation Service.

### Verification

Open a browser and navigate to `http://localhost:8761`. All five domain and infrastructure clients should appear with status `UP`:

```
API-GATEWAY · ZONE-MANAGEMENT-SERVICE · CROP-INVENTORY-SERVICE · AUTOMATION-CONTROL-SERVICE · SENSOR-TELEMETRY-SERVICE
```

A reference screenshot is available in the `docs/` directory.

---

## Authentication and JWT Security

All domain APIs are protected by the API Gateway's JWT filter. Requests without a valid Bearer Token will be rejected with `401 Unauthorized`.

### Step 1 — Obtain an Access Token

The `/api/auth/login` endpoint is exempt from the JWT filter and can be called directly:

```http
POST http://localhost:8080/api/auth/login?username=admin&password=admin
```

Copy the JWT string from the response body. Tokens are valid for **30 minutes**.

### Step 2 — Authenticate Subsequent Requests

Attach the token to all subsequent requests using the `Authorization` header:

```
Authorization: Bearer <your_token_here>
```

A fully pre-configured Postman Collection (`AGMS_Postman_Collection.json`) is included in the root directory. Import it into Postman and paste the generated token into the parent folder's **Authorization** tab to test all endpoints without manual header configuration.

---

## API Endpoint Reference

All client requests are routed through the **API Gateway on port `8080`**.

### Authentication

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Authenticate and receive a JWT access token. |

### Zone Management

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/zones` | Create a zone and register a corresponding device with the External IoT API. |
| `GET` | `/api/zones/{id}` | Retrieve thresholds and metadata for a specific zone. |
| `PUT` | `/api/zones/{id}` | Update minimum and maximum temperature thresholds. |
| `DELETE` | `/api/zones/{id}` | Remove a zone from the system. |

### Crop Inventory

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/crops` | Register a new crop batch. |
| `GET` | `/api/crops` | View current crop inventory. |
| `PUT` | `/api/crops/{id}/status` | Advance lifecycle stage (`SEEDLING`, `VEGETATIVE`, `HARVESTED`). |

### Sensor Telemetry

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/sensors/latest` | Returns the most recently fetched external telemetry reading. |

### Automation & Control

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/automation/process` | Internal endpoint for sensor data ingestion (called by Sensor Telemetry Service). |
| `GET` | `/api/automation/logs` | View all triggered automation actions (`TURN_FAN_ON`, `TURN_HEATER_ON`, `NORMAL`). |

---

## End-to-End Data Flow

```
External IoT API
      |
      | (every 10s, authenticated)
      v
Sensor Telemetry Service
      |
      | POST /api/automation/process
      v
Automation & Control Service
      |
      | GET /api/zones/{id} (via OpenFeign)
      v
Zone Management Service
      |
      | returns min/max thresholds
      v
Rule Engine evaluates → logs action to MySQL
```

---

## Documentation and Testing Assets

- `docs/Eureka dashboard screenshot.png` — Screenshot confirming all services registered as `UP`
- `AGMS_Postman_Collection.json` — Exported Postman collection with all tested endpoints