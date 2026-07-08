# MilkKhaata

MilkKhaata is a secure dairy management backend designed to help dairy businesses manage customers, milk production, deliveries, billing, payments, expenses, subscriptions, dashboards, and reports.

## Features

- User registration and login with JWT authentication
- Secure password hashing with BCrypt
- Customer management
- Daily milk production tracking
- Customer milk delivery tracking
- Bill generation and bill status management
- Customer payment recording
- Expense tracking by category
- Dashboard summaries
- Monthly and yearly business reports
- Subscription and plan management
- Role-based user structure
- Centralized validation and exception handling
- Environment-based secret configuration
- Configurable CORS support

## Tech Stack

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL
- H2 Database for testing
- JWT
- Maven
- Lombok

## Project Structure

```text
src/main/java/com/milkkhaata
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entities
├── exception
├── repository
├── security
└── service

```

## Environment Variables

Create the required environment variables before starting the application:

```text
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password
JWT_SECRET=your_jwt_secret
DEV_SUBSCRIPTION_ENABLED=false
CORS_ALLOWED_ORIGINS=http://localhost:3000
```

Use `.env.example` as a reference. Never commit real credentials or secrets.

## Run the Application

On Windows:

```bash
.\mvnw.cmd spring-boot:run
```

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

## Run Tests

On Windows:

```bash
.\mvnw.cmd clean test
```

Tests use an isolated H2 in-memory database and do not require the production MySQL database.

## API Modules

The backend provides APIs for:

- Authentication
- User profile
- Customers
- Milk production
- Milk deliveries
- Bills
- Customer payments
- Expenses
- Dashboard
- Reports
- Plans
- Subscriptions

## Security

- JWT-based stateless authentication
- BCrypt password hashing
- Protected API endpoints
- Environment-based secret management
- Configurable CORS policy
- Standardized authentication and API error responses

## Status

Backend development, security hardening, testing, and initial deployment preparation are complete.

Frontend development is the next phase of the project.