## Build stage
FROM maven:3.8.6-jdk-11 AS builder
LABEL maintainer="jugovitskaroly@gmail.com"
WORKDIR /art-gallery-app
COPY pom.xml .
# Dependency download step to ensure all necessary dependencies are fetched before copying the source code.
RUN mvn dependency:resolve dependency:resolve-plugins
COPY src src
# Using 'package' goal without running tests to speed up the build. Tests can included by removing '-DskipTests'.
RUN mvn clean package -DskipTests

## Run stage
FROM openjdk:11-jre-slim
COPY --from=builder /art-gallery-app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080

# Health check
# Installation command for curl since it's not available in 'openjdk:11-jre-slim' by default.
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
HEALTHCHECK --interval=30s --timeout=30s --start-period=10s --retries=3 CMD curl -f http://localhost:8080/actuator/health || exit 1

# Non-Root User
# Adjusted to use 'adduser' and 'addgroup' commands compatible with 'openjdk:11-jre-slim' image.
RUN groupadd -r spring && useradd -r -gspring spring
USER spring:spring

# Volume for Logs
VOLUME /log
