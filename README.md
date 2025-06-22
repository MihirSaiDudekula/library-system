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

## 🛡️ Future Enhancements

1. **Service Discovery** with Eureka
2. **API Gateway** with Spring Cloud Gateway
3. **Distributed Tracing** with Sleuth/Zipkin
4. **Containerization** with Docker
5. **Security** with JWT Authentication
6. **Event Sourcing** for better data consistency

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
