# ECommerce Backend

A RESTful e-commerce backend built with **Java, Spring Boot, Spring Security, JWT, JPA/Hibernate, and PostgreSQL**.

## Features

- User registration and authentication
- JWT-based authentication
- Role-based authorization (`CUSTOMER` / `ADMIN`)
- User profile management
- Password change
- Product management
- Category management
- Shopping cart management
- Order placement and order status management
- Address management
- Payment creation and processing
- Global exception handling
- Input validation
- PostgreSQL database integration

## Tech Stack

- **Java 21**
- **Spring Boot**
- Spring Web
- Spring Data JPA
- Spring Security
- JWT (JJWT)
- Hibernate
- PostgreSQL
- Maven
- Lombok

## Project Structure

```text
src/main/java/com/krishna/ecommerce
├── controller
├── dto
├── exception
├── model
├── repository
├── security
└── service
```

## Security

The application uses **JWT authentication** with stateless Spring Security.

- Public endpoints:
  - User registration
  - Login
- Authenticated users can access their own profile, cart, orders, addresses, and payments.
- Admin users can manage users, products, and categories.
- Admin users can process payments and update order status.

## Configuration

Sensitive configuration is supplied through environment variables.

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/ecommerce}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}

jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION:3600000}
```

Do **not** commit real database passwords or JWT secrets to the repository.

## Running the Project

Make sure PostgreSQL is running and the required environment variables are configured.

Using the Maven wrapper on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## API Overview

| Module | Main Operations |
|---|---|
| Authentication | Register, Login |
| Users | Profile, Password, Admin user management |
| Products | Create, Read, Update, Delete |
| Categories | Create, Read, Update, Delete |
| Cart | Add, View, Update, Remove |
| Orders | Place, View, Update Status |
| Addresses | Create, View, Update, Delete |
| Payments | Create, View, Process |

## Author

**Krishna Verma**

Computer Science and Engineering
