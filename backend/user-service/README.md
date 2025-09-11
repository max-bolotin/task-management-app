# User Service

Microservice for user authentication and management in the task management application. Built with
Spring Boot, Spring Security, and PostgreSQL.

## Description

The User Service handles:

- **Authentication** - JWT-based user registration, login, and token validation
- **User management** - CRUD operations for user accounts
- **Security** - BCrypt password hashing and role-based access control
- **Admin seeding** - Automatic admin user creation on startup

## Configuration

- **Port**: 8081
- **Database**: PostgreSQL (taskdb)
- **API Documentation**: http://localhost:8081/swagger-ui.html
- **JWT Expiration**: 24 hours (configurable)

## Authentication Endpoints

### POST `/auth/register`

Register a new user account.

**Request:**

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Response (201):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "email": "john@example.com",
    "name": "John Doe",
    "role": "USER"
  }
}
```

### POST `/auth/login`

Authenticate user and receive JWT token.

**Request:**

```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response (200):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "email": "john@example.com",
    "name": "John Doe",
    "role": "USER"
  }
}
```

### GET `/auth/me`

Get current authenticated user profile.

**Headers:** `Authorization: Bearer <token>`

**Response (200):**

```json
{
  "id": 1,
  "email": "john@example.com",
  "name": "John Doe",
  "role": "USER"
}
```

## User Management Endpoints

### GET `/users`

Get all users (requires authentication).

**Headers:** `Authorization: Bearer <token>`

**Response (200):**

```json
[
  {
    "id": 1,
    "email": "admin@taskapp.com",
    "name": "Administrator",
    "role": "ADMIN"
  },
  {
    "id": 2,
    "email": "john@example.com",
    "name": "John Doe",
    "role": "USER"
  }
]
```

### GET `/users/{id}`

Get user by ID (requires authentication).

**Headers:** `Authorization: Bearer <token>`

**Response (200):**

```json
{
  "id": 1,
  "email": "john@example.com",
  "name": "John Doe",
  "role": "USER"
}
```

### PATCH `/users/{id}`

Update user information (requires authentication).

**Headers:** `Authorization: Bearer <token>`

**Request:**

```json
{
  "name": "John Smith",
  "email": "johnsmith@example.com"
}
```

**Response (200):**

```json
{
  "id": 1,
  "email": "johnsmith@example.com",
  "name": "John Smith",
  "role": "USER"
}
```

### DELETE `/users/{id}`

Delete user account (requires authentication).

**Headers:** `Authorization: Bearer <token>`

**Response (204):** No content

## Other Endpoints

### GET `/roles`

Get all available user roles.

**Response (200):**

```json
[
  "USER",
  "ADMIN"
]
```

### GET `/health`

Health check endpoint.

**Response (200):**

```json
{
  "status": "UP"
}
```

## Error Responses

| Code | Description                        | Example Response                       |
|------|------------------------------------|----------------------------------------|
| 400  | Bad Request - Validation error     | `{"message": "Email already exists"}`  |
| 401  | Unauthorized - Invalid credentials | `{"message": "Invalid credentials"}`   |
| 403  | Forbidden - Invalid/expired token  | `{"message": "Access denied"}`         |
| 404  | Not Found - User doesn't exist     | `{"message": "User not found"}`        |
| 500  | Internal Server Error              | `{"message": "Internal server error"}` |

## Security Features

- **Password Hashing**: BCrypt with 12 salt rounds for secure password storage
- **Password Strength**: Enforced pattern (8+ chars, uppercase, lowercase, digit, special char)
- **JWT Tokens**: HS256 signed tokens with configurable expiration
- **Environment-based Secrets**: JWT secret configurable via environment variables
- **CORS Support**: Configured for frontend integration
- **Role System**: USER and ADMIN roles for future authorization
- **Admin Seeding**: Auto-creates admin user on first startup

## Configuration

### Environment Variables

| Variable         | Default                                         | Description                            |
|------------------|-------------------------------------------------|----------------------------------------|
| `JWT_SECRET`     | `myVerySecretKeyForJWTTokenGeneration123456789` | JWT signing secret                     |
| `JWT_EXPIRATION` | `86400000`                                      | Token expiration in milliseconds (24h) |
| `ADMIN_EMAIL`    | `admin@taskapp.com`                             | Default admin email                    |
| `ADMIN_PASSWORD` | `admin123`                                      | Default admin password                 |

### Database Configuration

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/taskdb
    username: taskapp
    password: secret
```

## Running the Service

### **Option 1: With Custom JWT Secret (Recommended)**

```bash
# Set JWT secret as environment variable
export JWT_SECRET="your-super-long-random-secret-key-at-least-32-characters-long"
mvn spring-boot:run

# Or inline
JWT_SECRET="your-super-long-random-secret-key-at-least-32-characters-long" mvn spring-boot:run
```

### **Option 2: Default Secret (Development Only)**

```bash
# Uses default secret (not secure for production)
mvn spring-boot:run
```

### **IntelliJ IDEA Setup:**

1. Go to `Run` → `Edit Configurations...`
2. Select User Service configuration
3. In `Environment variables` add:
   `JWT_SECRET=your-super-long-random-secret-key-at-least-32-characters-long`
4. Click `OK` and run normally

### **Production Deployment:**

```bash
mvn package
JWT_SECRET="your-production-secret" java -jar target/user-service-1.0-SNAPSHOT.jar
```

## Testing

Use the Swagger UI at http://localhost:8081/swagger-ui.html for interactive API testing.

**Quick Test Flow:**

1. Register a new user via `/auth/register`
2. Copy the JWT token from the response
3. Use the token in Authorization header: `Bearer <token>`
4. Test other endpoints with the authenticated token

## Dependencies

- Spring Boot 3.3.2
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL JDBC driver
- BCrypt password encoder
- OpenAPI/Swagger UI
- Common module (shared DTOs and enums)