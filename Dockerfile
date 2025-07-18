# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21-alpine AS build

# Set working directory
WORKDIR /app

# Copy the project files
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Environment variables for configuration
# Define default values for environment variables
# In production, sensitive values should be provided via Docker secrets or environment variables
ENV SERVER_PORT=8080 \
    SPRING_DATASOURCE_URL=jdbc:postgresql://100.64.64.201:5432/clinica \
    SPRING_DATASOURCE_USERNAME=clinica \
    # Password should be provided at runtime in production
    SPRING_DATASOURCE_PASSWORD=clinica \
    KAFKA_URL=100.64.64.202:9092 \
    WHISPER_URL=http://100.64.64.203:5001/transcrever \
    DEEPSEEK_GENERATE_URL=http://100.64.64.203:11434/api/generate \
    DEEPSEEK_GENERATE_MODEL=deepseek-r1:7b \
    DEEPSEEK_EMBEDDING_URL=http://100.64.64.203:11434/api/embeddings \
    DEEPSEEK_EMBEDDING_MODEL=nomic-embed-text \
    QDRANT_SESSION_URL=http://100.64.64303:6333/collections/sessions/points \
    REDIS_URL=redis://100.64.64.202:6379 \
    DEEPGRAM_API_URL=https://api.deepgram.com/v1/listen \
    # API key should be provided at runtime in production
    DEEPGRAM_API_KEY=b6ede36ecfafc81f95214ce9a16e694658f1e4a7 \
    TRANSCREVER_SERVICE=deepgram

# Expose the application port
EXPOSE ${SERVER_PORT}

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]