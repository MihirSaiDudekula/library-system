# Library Management System - Microservices

A distributed library management system built with Spring Boot microservices architecture.

## 🏗️ Architecture

```
+----------------+     +----------------+     +------------------+
|                |     |                |     |                  |
|  Book Service  |     |  User Service  |     | Borrowing Service |
|  (Port: 8081)  |     |  (Port: 8082)  |     |   (Port: 8083)   |
|                |     |                |     |                  |
+----------------+     +----------------+     +------------------+
```

## 📚 Services Overview

### 1. Book Service
- **Port**: 8081
- **Database**: H2 (In-memory)
- **H2 Console**: http://localhost:8081/h2-console

#### API Endpoints

| Method | Endpoint              | Description                      |
|--------|-----------------------|----------------------------------|
| GET    | /api/books           | Get all books                   |
| GET    | /api/books/{id}      | Get a specific book by ID       |
| POST   | /api/books           | Add a new book                  |
| GET    | /api/books/available | Get all available books         |


#### Example Request
```bash
# Add a new book
curl -X POST http://localhost:8081/api/books \
  -H "Content-Type: application/json" \
  -d '{"isbn":"1234567890","title":"Spring in Action","author":"Craig Walls"}'
```

---

### 2. User Service
- **Port**: 8082
- **Database**: H2 (In-memory)
- **H2 Console**: http://localhost:8082/h2-console

#### API Endpoints

| Method | Endpoint            | Description                      |
|--------|---------------------|----------------------------------|
| GET    | /api/users          | Get all users                   |
| GET    | /api/users/{id}     | Get a specific user by ID       |
| POST   | /api/users          | Register a new user             |


#### Example Request
```bash
# Register a new user
curl -X POST http://localhost:8082/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe","email":"john@example.com","name":"John Doe"}'
```

---

### 3. Borrowing Service
- **Port**: 8083
- **Database**: H2 (In-memory)
- **H2 Console**: http://localhost:8083/h2-console

#### API Endpoints

| Method | Endpoint                     | Description                          |
|--------|------------------------------|--------------------------------------|
| POST   | /api/borrowings             | Borrow a book                       |
| PUT    | /api/borrowings/{id}/return | Return a borrowed book              |
| GET    | /api/borrowings             | Get borrowings (filterable)         |


#### Example Requests
```bash
# Borrow a book
curl -X POST "http://localhost:8083/api/borrowings?bookId=1&userId=1&days=14"

# Return a book
curl -X PUT http://localhost:8083/api/borrowings/1/return

# Get borrowings by user
curl "http://localhost:8083/api/borrowings?userId=1"
```

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6.3 or higher

### Running the Services

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd library-system
   ```

2. **Start Book Service**
   ```bash
   cd book-service
   mvn spring-boot:run
   ```

3. **Start User Service** (in a new terminal)
   ```bash
   cd user-service
   mvn spring-boot:run
   ```

4. **Start Borrowing Service** (in a new terminal)
   ```bash
   cd borrowing-service
   mvn spring-boot:run
   ```

## 🛠️ Database Access

Each service has its own H2 in-memory database with a web console:
- **Book Service DB**: http://localhost:8081/h2-console
  - JDBC URL: jdbc:h2:mem:bookdb
  - Username: sa
  - Password: (leave empty)

- **User Service DB**: http://localhost:8082/h2-console
  - JDBC URL: jdbc:h2:mem:userdb
  - Username: sa
  - Password: password

- **Borrowing Service DB**: http://localhost:8083/h2-console
  - JDBC URL: jdbc:h2:mem:borrowingdb
  - Username: sa
  - Password: password

## 📝 Sample Workflow

1. **Add a book**
   ```bash
   curl -X POST http://localhost:8081/api/books \
     -H "Content-Type: application/json" \
     -d '{"isbn":"1234567890","title":"Spring in Action","author":"Craig Walls"}'
   ```

2. **Register a user**
   ```bash
   curl -X POST http://localhost:8082/api/users \
     -H "Content-Type: application/json" \
     -d '{"username":"john_doe","email":"john@example.com","name":"John Doe"}'
   ```

3. **Borrow the book**
   ```bash
   curl -X POST "http://localhost:8083/api/borrowings?bookId=1&userId=1&days=14"
   ```

4. **List all borrowings**
   ```bash
   curl http://localhost:8083/api/borrowings
   ```

# 🌐 Phase 2: Eureka Service Discovery

## Overview

Eureka is a **Service Discovery** tool from Netflix that acts as a service registry for microservices. It enables services to find and communicate with each other without hardcoded hostnames and ports.

## 🏗️ Architecture

```
+----------------+     +----------------+     +------------------+
|                |     |                |     |                  |
|  Book Service  |     |  User Service  |     | Borrowing Service |
|  (Port: 8081)  |     |  (Port: 8082)  |     |   (Port: 8083)   |
|                |     |                |     |                  |
+--------+-------+     +-------+--------+     +---------+--------+
         |                   |                      |
         |                   |                      |
         v                   v                      v
        +--------------------------------------------------+
        |                                                  |
        |               Eureka Discovery Server              |
        |                   (Port: 8761)                     |
        |                                                  |
        +--------------------------------------------------+
```

## 📋 Services Registration

| Service           | Port | Eureka Service Name   | Status  |
|-------------------|------|----------------------|---------|
| Discovery Service | 8761 | discovery-service    | ✅ Live |
| Book Service     | 8081 | book-service         | ✅ Live |
| User Service     | 8082 | user-service         | ✅ Live |
| Borrowing Service| 8083 | borrowing-service    | ✅ Live |

## 🚀 How to Start

1. **Start Discovery Server**
   ```bash
   cd discovery-service
   mvn spring-boot:run
   ```
   - Access Eureka Dashboard: http://localhost:8761

2. **Start Other Services** (in any order)
   ```bash
   # Terminal 2
   cd book-service
   mvn spring-boot:run

   # Terminal 3
   cd user-service
   mvn spring-boot:run

   # Terminal 4
   cd borrowing-service
   mvn spring-boot:run
   ```

## 🔍 Verifying the Setup

1. Open the Eureka Dashboard: http://localhost:8761
2. You should see all three services registered under "Instances currently registered with Eureka"
3. Each service will be listed with its service ID and status "UP"

## 🔄 Service Discovery in Action

Services can now discover each other using their service names. For example, to call the book service from another service:

```java
@Autowired
private DiscoveryClient discoveryClient;

public void someMethod() {
    List<ServiceInstance> instances = discoveryClient.getInstances("book-service");
    ServiceInstance instance = instances.get(0);
    String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/api/books";
    // Make HTTP request to the URL
}
```

## ⚙️ Configuration Details

### Discovery Service (`discovery-service`)
- Port: 8761
- Self-registration: Disabled
- Peer awareness: Not configured (single instance)

### Client Services (Book, User, Borrowing)
- Auto-registration with Eureka: Enabled
- Health check: Automatic (via Spring Boot Actuator)
- Heartbeat: Every 30 seconds (default)
- Lease renewal: Every 30 seconds (default)
- Lease expiration: 90 seconds (default)

## 🔄 Service-to-Service Communication

With Eureka in place, services can communicate using service names instead of hardcoded URLs. For example:

```
http://book-service/api/books/1
http://user-service/api/users/1
```

## 🔍 Eureka Dashboard

The Eureka dashboard provides:
- List of all registered instances
- Status of each instance (UP, DOWN, OUT_OF_SERVICE)
- Metadata about each instance
- Basic health information

## 📈 Next Steps

1. **High Availability**: Set up multiple Eureka servers for redundancy
2. **Load Balancing**: Use Ribbon for client-side load balancing
3. **API Gateway**: Implement Spring Cloud Gateway for routing
4. **Resilience**: Add circuit breakers with Resilience4j
5. **Distributed Tracing**: Implement with Sleuth and Zipkin

## 🛡️ Future Enhancements

1. **Service Discovery** with Eureka
2. **API Gateway** with Spring Cloud Gateway
3. **Distributed Tracing** with Sleuth/Zipkin
4. **Containerization** with Docker
5. **Security** with JWT Authentication
6. **Event Sourcing** for better data consistency

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
