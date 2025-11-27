# Multi-stage Dockerfile for building and running the Spring Boot app using Temurin 21

# Build stage with Maven and Temurin 21
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml ./
COPY src/ ./src/

# Avoid downloading dependencies on every build
RUN mvn -B -DskipTests dependency:go-offline
RUN mvn -B -DskipTests package

# Runtime stage using Temurin 21
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
ARG JAR_FILE=target/backend-1.0.0.jar
COPY --from=build /workspace/${JAR_FILE} /app/backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/backend.jar"]
