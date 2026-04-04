# Finance Management System

A Spring Boot backend application for managing users, roles, and transactions, with JWT authentication and role-based access control.

## Features
- User Registration & Login
- Role-Based Access (Viewer, Analyst, Admin)
- Transaction CRUD with soft delete
- Dashboard: Total Income, Expense, Balance
- Forgot Password & Reset Token
- Swagger (OpenAPI) API documentation
- JWT Authentication

...
## Tech Stack
- Java 17 / Spring Boot 3
- Spring Security + JWT
- Spring Data JPA + Hibernate
- MySQL / H2 Database
- Maven
- Swagger (OpenAPI) for API documentation
- ## Setup & Installation

1. Clone the repository:
```bash
git clone https://github.com/pavaneethreddyberravalli/Finance-dataprocessing-and-acess-control.git
cd Finance-dataprocessing-and-acess-control

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/finance_db
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: update

mvn clean install
mvn spring-boot:run

http://localhost:8080/swagger-ui/index.html


---

### **3. Roles & Permissions**
Add a clear table:

```markdown
## Roles & Permissions

| Role     | Access Level                                      |
|---------|--------------------------------------------------|
| VIEWER  | View dashboard & transactions                     |
| ANALYST | View transactions & insights                     |
| ADMIN   | Full access: manage users and transactions       |


## API Endpoints

### Auth
| Method | Endpoint                        | Description                          |
|--------|---------------------------------|--------------------------------------|
| POST   | /api/auth/register               | Register a new user                  |
| POST   | /api/auth/login                  | Login to get JWT access & refresh tokens |
| POST   | /api/auth/refresh                | Refresh JWT token using refresh token |
| POST   | /api/auth/forgot-password        | Generate reset token                 |
| POST   | /api/auth/reset-password         | Reset password using token           |

### Transactions
| Method | Endpoint                           | Description                            | Role       |
|--------|------------------------------------|----------------------------------------|------------|
| GET    | /api/transactions                  | Get all transactions (with filters)    | VIEWER+    |
| GET    | /api/transactions/{id}             | Get transaction by ID                   | VIEWER+    |
| POST   | /api/transactions                  | Create transaction                       | ADMIN      |
| PUT    | /api/transactions/{id}             | Update transaction                       | ADMIN      |
| DELETE | /api/transactions/{id}             | Soft delete transaction                  | ADMIN      |

### Dashboard
| Method | Endpoint                           | Description                           | Role       |
|--------|------------------------------------|---------------------------------------|------------|
| GET    | /api/dashboard/summary             | Get total income, expense, and balance | VIEWER+    |

### Example Requests

**Login:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}



{
  "type": "CREDIT",
  "category": "Salary",
  "amount": 5000.0,
  "description": "Monthly salary",
  "date": "2026-04-01"
}



---

### **6. Assumptions**
```markdown
## Assumptions
- Users have default role VIEWER
- Transactions support soft delete
- JWT access token expiry is short; refresh token extends session
- Reset token expires in 30 minutes




## Architecture / Flow Diagram

![Finance Management System Architecture](docs/finance-architecture.png)

- Users → AuthController → JWT + Roles
- TransactionsController → CRUD operations
- DashboardController → Summary
- Services → Business logic
- Repositories → Database access



## Contact

**Pavaneeth Reddy Berravalli**  
GitHub: [https://github.com/pavaneethreddyberravalli](https://github.com/pavaneethreddyberravalli)