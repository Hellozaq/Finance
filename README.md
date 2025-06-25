
💰 Personal Finance Manager - Spring Boot REST API

---

📋 Project Overview

The Personal Finance Manager is a secure RESTful API built with Spring Boot 3.x, using Java 17, Spring Security + JWT, Spring Data JPA, and an H2 in-memory database. It allows users to:

Register and log in securely.

Add, view, update, and delete financial transactions.

Calculate and retrieve current balance.

Access the API documentation via Swagger UI.

---

🏗️ Tech Stack

Java 17

Spring Boot

Spring Security & JWT

Spring Data JPA

H2 Database

JUnit 5 + Mockito

Swagger UI

---

🔐 API Security

Uses JWT for stateless authentication.

Spring Security protects all endpoints except /auth/register and /auth/login.

Passwords are hashed with BCrypt.

JWT is verified using a custom filter (JwtFilter).

Security rules are defined in SecurityConfig.java.

---

🔧 Main Features

🔑 Authentication

POST /auth/register: Register a user.

POST /auth/login: Log in with email and password, receive JWT token.


💸 Transactions

POST /transactions: Add a new transaction.

GET /transactions: Get all user transactions.

PUT /transactions: Update a transaction.

DELETE /transactions/delete: Delete a transaction (ID in JSON).

GET /transactions/balance: Calculate current balance.

---

📚 Swagger UI

Swagger API Docs available at:
📎 http://localhost:8080/swagger-ui/index.html

---

🛠️ How the Project Works

1. User Registration/Login:
User sends credentials → Password is hashed and stored securely → On login, token is generated using JwtUtil.


2. Token Validation:
Token is included in the Authorization header → Validated by JwtFilter → If valid, request proceeds.


3. Transaction Handling:
Authenticated users can add/update/delete their transactions.
The balance is computed by summing incomes and subtracting expenses.


4. Database:
Uses H2 in-memory DB. Access it at:
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:finance-db

---

🧪 Testing

Unit tests created using JUnit 5 and Mockito:

Controllers: AuthControllerTest, TransactionControllerTest

DTOs and Exception Handling: RegisterRequestTest, TransactionDTOTest, GlobalExceptionHandlerTest

---

🚧 Challenges Faced & Resolutions

🔧 During Main Project

1. JWT Token Parsing Error

Issue: MalformedJwtException: Malformed JWT JSON

Fix: The token string was corrupted or invalid. Corrected token handling logic in tests and ensured token structure was preserved.


2. H2 Console Not Displaying

Issue: Browser blocked iframe due to X-Frame-Options: DENY

Fix: Configured Spring Security to allow frame access for /h2-console path.


3. 403 Forbidden Error (CSRF)

Issue: Postman and frontend blocked due to missing CSRF token.

Fix: Disabled CSRF in SecurityConfig as we use stateless JWT authentication.


4. JWT Filter Not Triggering Properly

Cause: Filter order or Spring Security chain misconfiguration.

Fix: Verified filter chain and correctly registered JwtFilter before UsernamePasswordAuthenticationFilter.


5. Validation Not Triggering

Issue: Bean validation annotations not being enforced.

Fix: Ensured @Valid used in controller method parameters, and input fields had the necessary constraints.


🧪 During Testing

1. Mockito Error: doNothing() only works on void methods

Fix: Changed to Mockito.when(...).thenReturn(...) for non-void service mocks.


2. Record DTOs Had No Default Constructor

Fix: For tests, replaced records with POJOs or used @JsonProperty mappings.


3. Invalid Token Use in Tests

Fix: Real JWTs generated via login call in @BeforeEach method in integration tests.


4. Tests Failing Due to Path Variables

Fix: Adjusted test logic to match how the app uses JSON in request body instead of URL path variables.

---


