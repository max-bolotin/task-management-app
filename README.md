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
│   ├── notification-service/      # Micronaut (notifications)
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

### Prerequisites

- Java 21+
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL & MongoDB (via Docker)

### 1. Start Databases

```bash
docker compose up postgres mongo -d
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
# User Service (Spring Boot)
cd backend/user-service
mvn spring-boot:run

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

| Service              | URL                       | Status         | Documentation       |
|----------------------|---------------------------|----------------|---------------------|
| User Service         | http://localhost:8081     | 🔄 In Progress | `/swagger-ui`       |
| **Project Service**  | **http://localhost:8082** | **✅ Complete** | **`/q/swagger-ui`** |
| Notification Service | http://localhost:8083     | 🔄 In Progress | `/swagger-ui`       |
| Activity Service     | http://localhost:8084     | 🔄 In Progress | `/docs`             |
| Frontend             | http://localhost:3000     | 📋 Planned     | -                   |

---

## 📝 Current Status

### ✅ Completed

- **Project Service** - Complete with all features:
    - ✅ Full CRUD for projects and tasks
    - ✅ Task transitions and status management
    - ✅ Task assignment to users
    - ✅ Comment system for tasks
    - ✅ Search functionality (by text, project, assignee)
    - ✅ Global exception handling
    - ✅ Service layer architecture
    - ✅ Comprehensive test coverage (31 tests)
- **Common Module** - Shared DTOs and enums
- **Database Setup** - PostgreSQL and MongoDB via Docker
- **API Documentation** - OpenAPI/Swagger for all services

### 🔄 In Progress

- **User Service** - Authentication and user management
- **Notification Service** - In-app notifications
- **Activity Service** - Audit logging

### 📋 Planned

- **Frontend** - React UI
- **JWT Authentication** - Cross-service security
- **Service Integration** - Inter-service communication
- **Side Effects** - Event publishing to activity/notification services

---

## 🎯 MVP Roadmap

### Project Service ✅ COMPLETE

- [x] **Project & Task CRUD** - Create, read, update, delete operations
- [x] **Task Transitions** - Status workflow management
- [x] **Task Assignment** - Assign tasks to users
- [x] **Comment System** - Task comments with CRUD operations
- [x] **Search Functionality** - Text search and filtering
- [x] **Exception Handling** - Global exception mapper
- [x] **Service Architecture** - Clean separation of concerns
- [x] **Test Coverage** - Comprehensive test suite

### Remaining Services

- [x] **Database Integration** - PostgreSQL with proper schema
- [x] **API Documentation** - Swagger UI for testing
- [ ] **User Authentication** - JWT-based security
- [ ] **Notification System** - Real-time updates
- [ ] **Activity Logging** - Audit trail
- [ ] **Frontend UI** - React application
- [ ] **Service Integration** - Cross-service communication
- [ ] **Docker Deployment** - Full containerization