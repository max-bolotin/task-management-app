# ---------- Build ----------
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /build

# Copy root pom and sibling modules so parent POM resolves during build
COPY pom.xml ./pom.xml
COPY backend/common ./backend/common
COPY backend/user-service ./backend/user-service
COPY backend/project-service ./backend/project-service
COPY backend/notification-service ./backend/notification-service
COPY backend/activity-service ./backend/activity-service

RUN mvn clean package -DskipTests -pl backend/activity-service -am

# ---------- Run ----------
FROM eclipse-temurin:21-jdk
WORKDIR /app
# shaded jar attached with classifier "all"
COPY --from=build /build/backend/activity-service/target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java","-jar","app.jar"]
