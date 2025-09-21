# Task Management App (TMA)

A lightweight yet enhanced **Jira/Trello clone** designed for **small teams, startups, and
individuals**.  
The goal is to provide a **simple, self-hosted project management tool** with a modular architecture
and modern technologies.

---

## ✨ Features (MVP Scope)

- **User Management** – register, login, authenticate users.
- **Projects** – create projects, assign users, manage lifecycle.
- **Tasks** – create, update, track tasks inside projects.
- **Notifications** – send and manage system notifications (e.g., task updates, mentions).
- **Activity Log** – audit trail of all events across projects and tasks.
- **Frontend (React)** – intuitive UI to interact with backend services.

---

## 🏗️ Architecture

The app is built as a **microservices-based system**.  
Each service is independent, but they communicate over APIs and share common models/utilities from a
`common` module.

### **Backend Services**

1. **Common Module** (`backend/common`)
    - Shared DTOs, constants, utilities.
    - Maven module included by other services.

2. **User Service** (`backend/user-service`, Spring Boot)
    - Handles user registration, authentication, and profiles.
    - Connects to PostgreSQL.
    - Provides endpoints for managing users and auth.

3. **Project Service** (`backend/project-service`, Quarkus)
    - Manages projects and tasks.
    - Connects to PostgreSQL.
    - Core business logic for project/task lifecycle.

4. **Notification Service** (`backend/notification-service`, Micronaut)
    - Manages in-app notifications.
    - Connects to MongoDB.
    - Provides REST endpoints for fetching and acknowledging notifications.

5. **Activity Service** (`backend/activity-service`, Ktor with Kotlin & Maven)
    - Provides an **audit log** of user and system actions.
    - Connects to MongoDB.
    - Tracks all activity (e.g., "Task created", "Comment added") for transparency and analytics.

---

### **Frontend**

- **React app** (`frontend`)
    - Clean, modern UI for interacting with all backend services.
    - Communicates with backend via REST APIs.
    - Dockerized for easy deployment.

---

## ⚙️ Tech Stack

- **Backend:**
    - Spring Boot (User Service)
    - Quarkus (Project Service)
    - Micronaut (Notification Service)
    - Ktor + Kotlin + Maven (Activity Service)
    - MongoDB (NoSQL) + PostgreSQL (SQL)
    - Apache Kafka (Event Streaming)

- **Frontend:** React (TypeScript planned)

- **Containerization:** Docker + Docker Compose

---

## 📂 Repository Structure

```
task-management-app/
│
├── backend/
│   ├── common/                    # Shared module (DTOs, enums, utils)
│   ├── user-service/              # Spring Boot (users & auth)
│   ├── project-service/           # Quarkus (projects & tasks) ✅
│   ├── notification-service/      # Micronaut (notifications) ✅
│   └── activity-service/          # Ktor + Kotlin + Maven (audit log)
│
├── frontend/                      # React app (UI)
│
├── docker-compose.yml             # Orchestrates all services
├── TODO.md                        # Development roadmap
└── README.md                      # This file
```

---

## 🚀 Running the App

### Frontend Only (Quick Start)

```bash
# Navigate to frontend
cd frontend

# Install dependencies (first time)
npm install

# Start development server
npm start
```

**Access:** http://localhost:3000  
**Test Login:** `test@example.com` / `password`

### Full Stack

### Prerequisites

- Java 21+
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL, MongoDB & Kafka (via Docker)

### 1. Start Infrastructure

```bash
# Start databases and Kafka
docker compose up postgres mongo zookeeper kafka kafka-ui -d

# Create Kafka topics
docker exec kafka kafka-topics --create --bootstrap-server localhost:9092 --topic activity-events --partitions 3 --replication-factor 1 --if-not-exists
docker exec kafka kafka-topics --create --bootstrap-server localhost:9092 --topic notification-events --partitions 3 --replication-factor 1 --if-not-exists
```

### 2. Build Common Module

```bash
cd backend/common
mvn clean install
```

### 3. Run Services

**Option A: IntelliJ IDEA (Recommended)**

- Import project as Maven multi-module
- Run each service individually

**Option B: Command Line**

```bash
# User Service (Spring Boot) - with custom JWT secret
cd backend/user-service
JWT_SECRET="your-super-long-random-secret-key-at-least-32-characters-long" mvn spring-boot:run

# Project Service (Quarkus) 
cd backend/project-service
mvn quarkus:dev

# Notification Service (Micronaut)
cd backend/notification-service
mvn mn:run

# Activity Service (Ktor)
cd backend/activity-service
mvn exec:java
```

**Option C: Docker (Full Stack)**

```bash
docker compose up --build
```

### 4. Access Services

| Service                  | URL                       | Status           | Documentation       |
|--------------------------|---------------------------|------------------|---------------------|
| **User Service**         | **http://localhost:8081** | **✅ Complete**   | **`/swagger-ui`**   |
| **Project Service**      | **http://localhost:8082** | **✅ Complete**   | **`/q/swagger-ui`** |
| **Notification Service** | **http://localhost:8083** | **✅ Complete**   | **`/swagger-ui`**   |
| **Activity Service**     | **http://localhost:8084** | **✅ Complete**   | **`/swagger-ui`**   |
| **Frontend**             | **http://localhost:3000** | **🔄 MVP Ready** | **React App**       |
| **Kafka UI**             | **http://localhost:9090** | **✅ Available**  | **Monitoring**      |

---

## 📝 Current Status

### ✅ Completed

- **Project Service** - Complete with all features:
    - ✅ Full CRUD for projects and tasks
    - ✅ Task transitions and status management
    - ✅ Task assignment to users (fixed to assign to project owner)
    - ✅ Comment system for tasks
    - ✅ Search functionality (by text, project, assignee)
    - ✅ Global exception handling
    - ✅ Service layer architecture
    - ✅ Event publishing for activity and notifications
    - ✅ Comprehensive test coverage (31 tests)
- **Activity Service** - Complete with all features:
    - ✅ Kafka event consumer for activity logging
    - ✅ MongoDB storage with audit trail
    - ✅ REST API for manual event creation
    - ✅ Failed event handling and dead letter queue
    - ✅ Service layer architecture
    - ✅ Comprehensive test coverage (9 tests)
    - ✅ OpenAPI/Swagger documentation
- **Notification Service** - Complete with all features:
    - ✅ Kafka event consumer for notification processing
    - ✅ MongoDB storage with user-targeted notifications
    - ✅ REST API for notification management (CRUD)
    - ✅ Event-to-message conversion system
    - ✅ Profile-based configuration (dev/prod)
    - ✅ Logging configuration with INFO level
    - ✅ OpenAPI/Swagger documentation
- **Common Module** - Shared DTOs, enums, and event factories
- **Database Setup** - PostgreSQL and MongoDB via Docker
- **Kafka Infrastructure** - Event streaming with Kafka, Zookeeper, and Kafka UI
- **Event-Driven Architecture** - Project service publishes events to activity/notification services
- **API Documentation** - OpenAPI/Swagger for all services

### ✅ User Service - Complete

- ✅ JWT-based authentication (register, login, refresh)
- ✅ User management CRUD operations
- ✅ BCrypt password hashing
- ✅ Role-based user system (USER, ADMIN)
- ✅ Admin user auto-seeding
- ✅ OpenAPI/Swagger documentation
- ✅ CORS configuration
- ✅ Security filter chain with JWT validation

### 🔄 In Progress

- **Frontend (React)** - MVP user interface with basic project/task management (MVP features
  implemented)

### 📋 Planned

- **Service Integration** - Inter-service communication and JWT validation
- **Frontend Authentication** - Connect React app to real User Service
- **Frontend Enhancements** - Advanced project management features

---

## 🎯 Recent Achievements (Latest Session)

### ✅ Notification Service Implementation

- **Fixed Task Auto-Assignment** - Tasks now auto-assign to project owner instead of hardcoded user
  ID 1
- **Added Missing Notification Events** - Task creation now publishes notification events
- **Kafka Consumer Integration** - Successfully consuming events from `notification-events` topic
- **MongoDB Integration** - Notifications properly saved to `notificationdb.notifications`
  collection
- **Profile Configuration** - Separate dev/prod configurations for Kafka bootstrap servers
- **Logging Configuration** - INFO level logging via logback.xml
- **Event Processing** - Converting Kafka events to user-friendly notification messages
- **REST API** - Complete CRUD operations for notification management

### 🔧 Technical Fixes

- **Kafka Deserialization** - Fixed JSON parsing issues with manual ObjectMapper approach
- **Environment Variables** - Profile-based configuration to handle Micronaut env var limitations
- **Logging Dependencies** - Added Logback for proper logging support
- **Event Flow** - End-to-end event flow from Project Service → Kafka → Notification Service →
  MongoDB

### 📊 Event Flow Status

1. **Project Service** ✅ - Publishes events to both `activity-events` and `notification-events`
   topics
2. **Activity Service** ✅ - Consumes `activity-events` and stores in MongoDB
3. **Notification Service** ✅ - Consumes `notification-events` and stores user notifications in
   MongoDB
4. **Frontend Integration** 🔄 - Ready for notification display implementation

---

## 🎯 MVP Roadmap

### Project Service ✅ COMPLETE

- [x] **Project & Task CRUD** - Create, read, update, delete operations
- [x] **Task Transitions** - Status workflow management
- [x] **Task Assignment** - Assign tasks to users (fixed to project owner)
- [x] **Comment System** - Task comments with CRUD operations
- [x] **Search Functionality** - Text search and filtering
- [x] **Exception Handling** - Global exception mapper
- [x] **Service Architecture** - Clean separation of concerns
- [x] **Event Publishing** - Activity and notification events
- [x] **Test Coverage** - Comprehensive test suite

### Notification Service ✅ COMPLETE

- [x] **Kafka Integration** - Event consumer with proper deserialization
- [x] **MongoDB Storage** - User-targeted notification persistence
- [x] **REST API** - Complete CRUD operations
- [x] **Event Processing** - Message conversion and user targeting
- [x] **Configuration** - Profile-based dev/prod setup
- [x] **Documentation** - OpenAPI/Swagger integration

### User Service ✅ COMPLETE

- [x] **Authentication Endpoints** - Register, login, profile management
- [x] **User Management** - CRUD operations for users
- [x] **JWT Security** - Token-based authentication with BCrypt
- [x] **Role System** - USER and ADMIN roles
- [x] **Admin Seeding** - Auto-create admin user on startup
- [x] **API Documentation** - Swagger UI with proper parameter handling
- [x] **Security Configuration** - CORS and JWT filter chain
- [x] **Database Integration** - PostgreSQL with JPA/Hibernate

### Activity Service ✅ COMPLETE

- [x] **Kafka Integration** - Event consumer for activity logging
- [x] **MongoDB Storage** - Activity event persistence
- [x] **REST API** - Manual event creation and retrieval
- [x] **WebSocket Support** - Real-time activity updates
- [x] **Error Handling** - Dead letter queue for failed events
- [x] **Documentation** - OpenAPI/Swagger integration

### Frontend MVP ✅ IMPLEMENTED

- [x] **Authentication Pages** - Login/Register with mock auth fallback
- [x] **Dashboard** - Project listing and creation
- [x] **Project Management** - CRUD operations for projects
- [x] **Task Management** - Kanban board with TODO/IN_PROGRESS/DONE states
- [x] **Activity Feed** - Real-time activity tracking
- [x] **Notifications** - Basic notification system UI
- [x] **Responsive Design** - Clean, modern interface
- [x] **API Integration** - Connects to backend services with fallbacks
- [x] **Routing** - Protected routes and navigation
- [x] **CORS Configuration** - Cross-origin requests enabled

### Remaining Tasks

- [x] **Database Integration** - PostgreSQL and MongoDB with proper schemas
- [x] **API Documentation** - Swagger UI for all services
- [x] **User Authentication** - JWT-based security
- [x] **Notification System** - Real-time notification processing
- [x] **Activity Logging** - Complete audit trail
- [x] **Frontend UI** - React application (MVP complete)
- [ ] **Service Integration** - Cross-service JWT validation
- [ ] **Docker Deployment** - Full containerization

### Future Frontend Enhancements 🚀

- [ ] **Custom Task Statuses** - User-defined workflow states beyond TODO/IN_PROGRESS/DONE
- [ ] **Sprint Planning** - Agile sprint management and planning tools
- [ ] **Time/Effort Estimation** - Task estimation and time tracking
- [ ] **Advanced Planning** - Roadmaps, milestones, and project planning
- [ ] **Real-time Collaboration** - Live updates and team collaboration
- [ ] **Advanced Reporting** - Analytics and project insights
- [ ] **Mobile Responsiveness** - Enhanced mobile experience
- [ ] **Drag & Drop** - Advanced task management interactions