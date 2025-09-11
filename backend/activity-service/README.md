# Activity Service

Microservice for activity logging and real-time updates in the task management application. Built
with Ktor (Kotlin), MongoDB, and Kafka.

## Features

- **Kafka Event Consumer** - Consumes activity events from Kafka topics
- **MongoDB Storage** - Stores activity events with audit trail
- **REST API** - Provides endpoints for retrieving activity data
- **WebSocket Support** - Real-time activity updates via WebSocket connections
- **Failed Event Handling** - Dead letter queue for failed event processing
- **CORS Support** - Cross-origin requests enabled for frontend integration

## Configuration

- **Port**: 8084
- **MongoDB**: Connection via `MONGO_URI` environment variable
- **Kafka**: Bootstrap servers via `KAFKA_BOOTSTRAP_SERVERS`
- **WebSocket**: Available at `ws://localhost:8084/ws/activities`
- **API Documentation**: http://localhost:8084/swagger-ui

## API Endpoints

### GET `/activities`

Retrieve all activity events.

**Response:**

```json
[
  {
    "id": "507f1f77bcf86cd799439011",
    "type": "TASK_CREATED",
    "projectId": "123",
    "taskId": "456",
    "userId": "789",
    "timestamp": "2023-09-07T10:30:00Z",
    "payload": {
      "taskTitle": "New Task"
    }
  }
]
```

### WebSocket `/ws/activities`

Real-time activity updates via WebSocket connection.

**Connection:**

```javascript
// Connect to all activities
const ws = new WebSocket('ws://localhost:8084/ws/activities');

// Connect to specific project activities
const ws = new WebSocket('ws://localhost:8084/ws/activities?projectId=123');
```

**Messages:**

```json
// Connection confirmation
{"type":"connection","status":"connected","projectId":"123"}

// Activity event
{
  "id": "507f1f77bcf86cd799439011",
  "type": "TASK_CREATED",
  "projectId": "123",
  "taskId": "456",
  "userId": "789",
  "timestamp": "2023-09-07T10:30:00Z",
  "payload": {
    "taskTitle": "New Task"
  }
}
```

## Running the Service

```bash
# Development mode
mvn exec:java

# Build and run
mvn package
java -jar target/activity-service-1.0-SNAPSHOT.jar
```

## Environment Variables

| Variable                  | Default                     | Description               |
|---------------------------|-----------------------------|---------------------------|
| `MONGO_URI`               | `mongodb://localhost:27017` | MongoDB connection string |
| `MONGO_DB`                | `activitydb`                | MongoDB database name     |
| `MONGO_COLLECTION`        | `events`                    | MongoDB collection name   |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092`            | Kafka bootstrap servers   |
| `PORT`                    | `8084`                      | Service port              |

## Dependencies

- Ktor (Kotlin web framework)
- MongoDB with KMongo driver
- Apache Kafka consumer
- Jackson for JSON processing
- WebSocket support for real-time updates