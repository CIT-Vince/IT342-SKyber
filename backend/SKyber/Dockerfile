# Stage 1: Build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
ARG JAR_FILE=target/SKyber-0.0.1-SNAPSHOT.jar
COPY --from=build /app/${JAR_FILE} app.jar
EXPOSE 8080

# Environment Variables (Optional)
# ENV FIREBASE_CONFIG='{"type":"service_account","project_id":"..."}'

ENTRYPOINT ["java", "-jar", "app.jar"]