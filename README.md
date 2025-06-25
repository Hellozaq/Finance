# 💰 Personal Finance Manager – REST API with Spring Boot

## 📋 Project Summary

The **Personal Finance Manager** is a secure RESTful API built using **Spring Boot 3**, **Java 17**, and **JWT authentication**. It allows users to manage their personal finances through registration, login, and full CRUD operations for financial transactions. The API also provides balance calculation and Swagger UI for easy testing.

---

## 🛠 Tech Stack

- Java 17  
- Spring Boot 3.x  
- Spring Security + JWT  
- Spring Data JPA  
- H2 In-Memory Database  
- JUnit 5 + Mockito  
- Swagger UI

---

## ⚙️ Setup & Requirements

### Prerequisites

- Java 17 installed  
- Maven installed  
- An IDE (e.g., IntelliJ IDEA, Eclipse)  
- Postman or similar REST client

### How to Run

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd finance
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the API documentation**
   - [Swagger UI](http://localhost:8080/swagger-ui/index.html)

5. **Access H2 Database Console (Dev only)**
   - [H2 Console](http://localhost:8080/h2-console)  
   - JDBC URL: `jdbc:h2:mem:finance-db`  
   - Username: `sa`  
   - Password: *(leave blank)*

---

## 🔐 API Security

- JWT-based stateless authentication
- Endpoints `/auth/register` and `/auth/login` are public
- All other endpoints require a valid JWT
- Passwords are hashed with BCrypt
- Token is validated via a custom filter (`JwtFilter`)
- Security configuration is defined in `SecurityConfig.java`

---

## ✨ Features

### 🔑 Authentication

- `POST /auth/register` → Register a new user  
- `POST /auth/login` → Log in with credentials and receive a JWT

### 💸 Transactions

- `POST /transactions` → Add a new transaction  
- `GET /transactions` → View all user transactions  
- `PUT /transactions` → Update a transaction  
- `DELETE /transactions/delete` → Delete a transaction (ID in JSON)  
- `GET /transactions/balance` → Get current financial balance

---

## 📚 API Documentation

Access all endpoints and test the API via Swagger UI:

📎 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🔄 Application Flow

1. **User Registration & Login**
   - New users register with email and password (hashed with BCrypt)
   - On successful login, a JWT is issued

2. **JWT Token Authentication**
   - JWT is sent in the `Authorization` header for protected endpoints
   - Token is validated by a custom filter before processing the request

3. **Transaction Management**
   - Authenticated users can add, edit, delete, and view their own transactions
   - Balance is computed as total income minus total expense

4. **Database**
   - Uses H2 in-memory database for development
   - Accessible at `/h2-console` for debugging

---

## ✅ Testing

Tests are written using **JUnit 5** and **Mockito** for:

- `AuthControllerTest`, `TransactionControllerTest`
- `RegisterRequestTest`, `TransactionDTOTest`, `GlobalExceptionHandlerTest`

---

## 🚧 Challenges Faced & Resolutions

### During Development

- **JWT Token Parsing Error**
  - _Issue_: MalformedJwtException  
  - _Fix_: Ensured proper token generation and structure in tests

- **H2 Console Blocked**
  - _Issue_: Blocked by browser due to `X-Frame-Options: DENY`  
  - _Fix_: Allowed frame access to `/h2-console` in security settings

- **403 Forbidden from Postman**
  - _Issue_: CSRF protection blocking requests  
  - _Fix_: Disabled CSRF in `SecurityConfig` due to JWT usage

- **JWT Filter Not Triggered**
  - _Issue_: Incorrect filter registration  
  - _Fix_: Registered `JwtFilter` before `UsernamePasswordAuthenticationFilter`

- **Validation Not Working**
  - _Issue_: `@Valid` annotations not enforced  
  - _Fix_: Ensured controller methods use `@Valid` and DTOs use validation annotations

### During Testing

- **Mockito Errors**
  - _Issue_: Used `doNothing()` on non-void methods  
  - _Fix_: Replaced with `when(...).thenReturn(...)`

- **DTO Record Issues**
  - _Issue_: No default constructor  
  - _Fix_: Used POJOs or configured Jackson with proper annotations

- **Invalid JWTs in Tests**
  - _Fix_: Generated real tokens in `@BeforeEach` setup

- **Incorrect Use of Path Variables**
  - _Issue_: Tests assumed ID in URL  
  - _Fix_: Updated tests to send ID in JSON body to match actual API

---
