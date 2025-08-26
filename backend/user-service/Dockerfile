# Build stage
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /build

# Copy parent POM
COPY pom.xml ./pom.xml

# Copy modules
COPY backend/common ./backend/common
COPY backend/user-service ./backend/user-service
COPY backend/project-service ./backend/project-service
COPY backend/notification-service ./backend/notification-service

# Build from root to resolve parent POM
RUN mvn clean package -DskipTests -pl backend/user-service -am

# Run stage
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /build/backend/user-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
