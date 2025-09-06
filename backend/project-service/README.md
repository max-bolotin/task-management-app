# Project Service

Microservice for managing projects and tasks in the task management application. Built with Quarkus
and PostgreSQL.

## Description

The Project Service handles:

- **Project management** - Create, read, delete projects with unique keys
- **Task management** - Create, read, update, delete tasks within projects
- **Task assignment** - Assign tasks to users and manage task status
- **Data persistence** - PostgreSQL database with Hibernate ORM

## Configuration

- **Port**: 8082
- **Database**: PostgreSQL (taskdb)
- **API Documentation**: http://localhost:8082/q/swagger-ui

## Endpoints

### Projects

| Method | Endpoint         | Description        |
|--------|------------------|--------------------|
| GET    | `/projects`      | List all projects  |
| GET    | `/projects/{id}` | Get project by ID  |
| POST   | `/projects`      | Create new project |
| DELETE | `/projects/{id}` | Delete project     |

### Tasks

| Method | Endpoint                          | Description            |
|--------|-----------------------------------|------------------------|
| GET    | `/projects/{projectId}/tasks`     | List tasks for project |
| POST   | `/projects/{projectId}/tasks`     | Create task in project |
| GET    | `/tasks/{taskId}`                 | Get task by ID         |
| PATCH  | `/tasks/{taskId}`                 | Update task            |
| DELETE | `/tasks/{taskId}`                 | Delete task            |
| POST   | `/tasks/{taskId}/assign/{userId}` | Assign task to user    |

## Request Examples

### Create Project

```json
POST /projects
{
  "name": "My Project",
  "key": "MYPROJ",
  "description": "Project description",
  "ownerId": 1
}
```

### Create Task

```json
POST /projects/1/tasks
{
  "title": "My Task",
  "description": "Task description",
  "status": "TODO"
}
```

### Update Task

```json
PATCH /tasks/1
{
  "title": "Updated Task",
  "status": "IN_PROGRESS",
  "assigneeId": 2
}
```

## Error Codes

| Code | Description                           | Example Response               |
|------|---------------------------------------|--------------------------------|
| 400  | Bad Request - Missing required fields | `"Project name is required"`   |
| 404  | Not Found - Resource doesn't exist    | `"Project not found"`          |
| 409  | Conflict - Duplicate key              | `"Project key already exists"` |
| 500  | Internal Server Error                 | Database connection issues     |

## Data Models

### Project

- `id` - Auto-generated ID
- `name` - Project name (required)
- `key` - Unique project key (required)
- `description` - Project description
- `ownerId` - Project owner ID (required)
- `createdAt` - Creation timestamp
- `updatedAt` - Last update timestamp

### Task

- `id` - Auto-generated ID
- `title` - Task title (required)
- `description` - Task description
- `status` - Task status (TODO, IN_PROGRESS, DONE)
- `projectId` - Parent project ID
- `assigneeId` - Assigned user ID
- `reporterId` - Reporter user ID
- `createdAt` - Creation timestamp
- `updatedAt` - Last update timestamp

## Running the Service

```bash
# Development mode
mvn quarkus:dev

# Build and run
mvn package
java -jar target/project-service-1.0-SNAPSHOT.jar
```

## Testing

```bash
# Run all tests
mvn test

# Build with tests
mvn package

# Skip tests during build
mvn package -DskipTests
```

### Test Coverage

- **ProjectResourceTest** - REST API endpoint testing (8 tests)
- **TaskResourceTest** - Task management endpoint testing (6 tests)
    - Task creation and retrieval
    - Task PATCH updates
    - Task assignment to users
    - Error handling for missing resources
- **ProjectEntityTest** - Entity lifecycle and persistence testing (3 tests)
- **TaskEntityTest** - Task entity operations testing (3 tests)
- **SimpleTest** - Basic logic and enum testing (2 tests)

### Status

✅ **All Tests Passing** - 22 tests run successfully  
✅ **Compilation Fixed** - All source and test files compile successfully  
✅ **Dependencies Resolved** - JPA, JAX-RS, and H2 test database configured  
✅ **Build Ready** - Project packages and tests execute without errors

## Dependencies

- Quarkus REST
- Hibernate ORM with Panache
- PostgreSQL JDBC driver
- OpenAPI/Swagger UI
- Common module (shared DTOs and enums)
- JUnit 5 & REST Assured (testing)
- H2 Database (test database)