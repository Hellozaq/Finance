✅ Personal Finance Manager – Spring Boot REST API

Tech Stack:
- Java 17
- Spring Boot 3.x
- Spring Security + JWT (JJWT 0.12.6)
- Spring Data JPA + Hibernate
- H2 in-memory database
- JUnit 5 + Mockito
- Swagger UI
- Maven

🚀 Features
- User registration & secure login (JWT + BCrypt)
- JWT-based authorization on all protected endpoints
- Add, update, delete, list financial transactions
- View total balance across all transactions
- H2 console for debugging in-memory DB
- Global exception handling & validation errors
- Swagger UI auto-documentation
- Complete unit & integration tests

📁 Endpoints

Auth:
- POST /auth/register – Register user
- POST /auth/login – Login and return JWT

Transactions (JWT required):
- POST /transactions – Add transaction
- GET /transactions – Get all transactions
- GET /transactions/balance – Calculate total balance
- PUT /transactions – Update a transaction
- DELETE /transactions/delete – Delete transaction (ID via JSON)

✅ How to Run
1. Build with Maven:
   mvn clean install
2. Run the app:
   mvn spring-boot:run
3. Access Swagger:
   http://localhost:8080/swagger-ui/index.html
4. H2 Console:
   http://localhost:8080/h2-console

🧪 Testing
- Controller-level integration tests with @SpringBootTest and MockMvc
- Manual JWT generation using POST /auth/login
- Transaction tests without using @WithMockUser
- Custom DTO validation tests
- 25 test cases passed ✅

🧱 Major Challenges Faced

1. JWT Token Validation Failure (MalformedJwtException)
   - Occurred due to mocking the wrong token format in tests.
   - Fixed by registering/logging in users in tests and extracting real tokens.

2. Validation Errors Not Returning 400
   - @Valid wasn't triggering due to missing @RequestBody or invalid input JSON structure.
   - Fixed by using JSON with empty strings and annotating controller DTO parameters correctly.

3. Cannot Resolve Symbol Errors
   - Multiple cases like Principal, WithMockUser, type() method, TransactionType enums, etc.
   - Solved by adding required imports or mocking security dependencies.

4. Mocking Failures in AuthControllerTest
   - Tried doNothing() on non-void methods like authenticate().
   - Fixed by switching to when(...).thenReturn(...) and using @SpringBootTest instead of @WebMvcTest.

5. Dependency Injection Issues with CustomUserDetailsService in Tests
   - Application failed to start due to missing bean in test context.
   - Fixed by avoiding test slicing (@WebMvcTest) and using full context via @SpringBootTest.

6. Mocking Principal for JWT Authenticated Users
   - Needed valid authentication context in integration tests instead of mocking users.
   - Used real registration + login requests to retrieve and pass actual tokens in headers.

7. ID Passed in JSON vs URL Confusion
   - Backend expected ID in JSON, not path param, for delete/update.
   - Tests were mistakenly using URL path variable.
   - Fixed by updating test payload to match controller contract.