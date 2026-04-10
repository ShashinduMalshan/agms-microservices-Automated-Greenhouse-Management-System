# 🌿 Automated Greenhouse Management System (AGMS)

The **Automated Greenhouse Management System (AGMS)** is a high-performance, cloud-native microservices platform designed for precision agriculture. The system automates climate monitoring and control by integrating real-time telemetry from IoT sensors with an intelligent rule engine to ensure optimal crop growth conditions.

## 🏗️ System Architecture
The platform is built using the **Spring Cloud** ecosystem, following a decoupled architecture for maximum scalability and resilience.

### 🛠️ Infrastructure Services
*   **Service Registry (Netflix Eureka):** Centralized service discovery for dynamic microservice networking.
*   **Config Server (Spring Cloud Config):** Centralized configuration management using a remote Git repository.
*   **API Gateway (Spring Cloud Gateway):** The unified entry point for all client traffic, providing dynamic routing and JWT-based security.

### 🌐 Domain Microservices
*   **Auth Service:** Manages user identity, registration, and JWT token issuance.
*   **Zone Management Service:** Defines greenhouse logical zones and manages environmental thresholds.
*   **Sensor Telemetry Service:** Acts as a real-time data bridge for external IoT telemetry ingestion.
*   **Automation & Control Service:** The system's "Decision Engine" that triggers climate actions (Fan/Heater) based on live data.
*   **Crop Inventory Service:** Monitors plant lifecycles from seedling stages to final harvest.

---

## 🛠️ Prerequisites & Configuration
*   **Java:** Version 21
*   **Build Tool:** Maven
*   **Database:** MySQL (Ensure schemas are created for each service)
*   **Centralized Config Repository:** (agms-config-repo](https://github.com/ShashinduMalshan/agms-config-repo.git))

---

## 🏁 Startup Instructions (Execution Order)
To ensure the system initializes correctly and services register with Eureka, please start them in the following sequence:

1.  **Service Registry** (Port: `8761`) - Wait for dashboard availability.
2.  **Config Server** (Port: `8888`) - Ensure it fetches the Git properties.
3.  **Auth Service** (Port: `8085`) - Required for security validation.
4.  **API Gateway** (Port: `8080`) - Main entry point.
5.  **Domain Services** (Zone, Crop, Automation) - Initialize business logic.
6.  **Sensor Service** (Port: `8082`) - Start last to initiate data transmission.

---

## 🛡️ External API Resilience (Technical Note)
The system is architected to interface with a Live External IoT Provider (`104.211.95.241`). 

**Important:** During the evaluation phase, it was noted that the external API might be unreachable or expired. To ensure the system remains functional, the `Sensor-Telemetry-Service` includes a **Hybrid Mock Mechanism**. This fallback generates simulated telemetry data to demonstrate the seamless end-to-end workflow (Sensor -> Automation -> DB Logging) without system downtime.

---

## 🧪 API Verification & Testing
A pre-configured **Postman Collection** is included in the project root for rapid testing.
*   **File:** `AGMS_Postman_Collection.json`
*   **How to Test:** Import the file, use the `/auth/login` endpoint to get a token, and include it as a **Bearer Token** in the Authorization header for all subsequent requests.

## 📊 System Monitoring
The operational health of the microservice ecosystem can be monitored via the Eureka Dashboard:
🔗 **URL:** (http://localhost:8761)  
*(Refer to `docs/eureka-dashboard.png` for the healthy status reference)*

---

### 👨‍💻 Developed By
*   **Name:** shasidu malshan
*   **Batch:** GDSE 71
*   **Institute:** IJSE - Institute of Java and Software Engineering
*   **Subject:** Software Architectures & Design Patterns II (Final Assignment)
