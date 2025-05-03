# Stage 1: Build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY backend/SKyber/pom.xml .
RUN mvn dependency:go-offline
COPY backend/SKyber/src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
ARG JAR_FILE=target/SKyber-0.0.1-SNAPSHOT.jar
COPY --from=build /app/${JAR_FILE} app.jar

# Use PORT environment variable (Render sets this automatically)
ENV PORT=8080
EXPOSE ${PORT}

# Environment Variables (Optional)
# ENV FIREBASE_CONFIG='{"type":"service_account","project_id":"..."}'

ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]