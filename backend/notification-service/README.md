# Notification Service

Microservice for managing in-app notifications in the task management application. Built with
Micronaut, MongoDB, and Kafka.

## Features

- **Kafka Event Consumer** - Consumes notification events from Kafka topics
- **MongoDB Storage** - Stores notifications with user targeting
- **REST API** - Provides endpoints for notification management
- **Event Processing** - Converts events to user-friendly messages
- **CORS Support** - Cross-origin requests enabled for frontend integration

## Configuration

- **Port**: 8083
- **MongoDB**: Connection via `MONGO_URI` environment variable
- **Kafka**: Profile-based configuration (see below)
- **API Documentation**: http://localhost:8083/swagger-ui

## Environment Profiles

### Development (default)

Uses hardcoded Kafka bootstrap servers for simplicity:

```yaml
kafka:
  bootstrap:
    servers: localhost:9092
```

### Production

Uses environment variables for production deployment:

```yaml
kafka:
  bootstrap:
    servers: ${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
```

**Note**: Micronaut has issues with environment variable substitution in Kafka configuration during
development, so we use profile-specific configurations to handle this.

## API Endpoints

### GET `/notifications/{userId}`

Retrieve all notifications for a user.

**Response:**

```json
[
  {
    "id": "507f1f77bcf86cd799439011",
    "userId": 123,
    "message": "You have been assigned a new task",
    "type": "TASK_ASSIGNED",
    "read": false,
    "createdAt": "2023-09-07T10:30:00Z"
  }
]
```

### POST `/notifications/{userId}`

Create a manual notification for a user.

**Request:**

```json
{
  "message": "Your task has been updated",
  "type": "TASK_UPDATE"
}
```

**Response (200):**

```json
{
  "id": "507f1f77bcf86cd799439011",
  "userId": 123,
  "message": "Your task has been updated",
  "type": "TASK_UPDATE",
  "read": false,
  "createdAt": "2023-09-07T10:30:00Z"
}
```

### PATCH `/notifications/{id}/read`

Mark a notification as read.

**Response (200):**

```json
{
  "id": "507f1f77bcf86cd799439011",
  "userId": 123,
  "message": "You have been assigned a new task",
  "type": "TASK_ASSIGNED",
  "read": true,
  "createdAt": "2023-09-07T10:30:00Z"
}
```

### DELETE `/notifications/{id}`

Delete a notification.

**Response (204):** No content

### GET `/test`

Health check endpoint.

**Response (200):** `"Notification Service is running!"`

### GET `/status`

Service status endpoint.

**Response (200):**

```json
{
  "status": "UP",
  "service": "notification-service"
}
```

## Event Processing

The service consumes events from the `notification-events` Kafka topic and converts them to
user-friendly messages:

- `TASK_ASSIGNED` → "You have been assigned a new task"
- `TASK_STATUS_CHANGED` → "Task's status has been changed"
- `TASK_COMMENTED` → "A task has been commented"
- `PROJECT_INVITATION` → "You were invited to a project"
- `MENTION` → "You were mentioned"

## Running the Service

### Development Mode (default)

```bash
mvn mn:run
```

### Production Mode

```bash
MICRONAUT_ENVIRONMENTS=prod mvn mn:run
```

### With Custom Environment Variables

```bash
MONGO_URI="mongodb://localhost:27017/notificationdb" \
KAFKA_BOOTSTRAP_SERVERS="localhost:9092" \
MICRONAUT_ENVIRONMENTS=prod \
mvn mn:run
```

## Environment Variables

| Variable                  | Default                                    | Description                         |
|---------------------------|--------------------------------------------|-------------------------------------|
| `MONGO_URI`               | `mongodb://localhost:27017/notificationdb` | MongoDB connection string           |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092`                           | Kafka bootstrap servers (prod only) |
| `MICRONAUT_ENVIRONMENTS`  | `dev`                                      | Active profile (dev/prod)           |

## Dependencies

- Micronaut Framework
- MongoDB with Micronaut Data
- Apache Kafka consumer
- Jackson for JSON processing
- Logback for logging
- Common module (shared DTOs and enums)

## Recent Updates

- ✅ **MongoDB Integration** - Full CRUD operations with notifications collection
- ✅ **Kafka Consumer** - Processes notification events from `notification-events` topic
- ✅ **Event Processing** - Converts Kafka events to user notifications
- ✅ **Profile Configuration** - Separate dev/prod configurations for Kafka
- ✅ **Logging Configuration** - INFO level logging via logback.xml
- ✅ **REST API** - Complete notification management endpoints