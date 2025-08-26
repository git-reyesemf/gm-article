# Use OpenJDK 21
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src src

# Make gradlew executable
RUN chmod +x ./gradlew

# Build the application
RUN ./gradlew build -x test

# Expose port
EXPOSE 8080

# Run the application
CMD ["sh", "-c", "echo 'DEBUG: Environment variables:' && echo 'DATABASE_URL='$DATABASE_URL && echo 'DB_USERNAME='$DB_USERNAME && echo 'DB_PASSWORD='$DB_PASSWORD && echo 'SCOPE_SUFFIX='$SCOPE_SUFFIX && echo 'Starting application...' && java -Dserver.port=${PORT:-8080} -Dspring.profiles.active=${SCOPE_SUFFIX:-test} -jar build/libs/gm-article-0.0.1-SNAPSHOT.jar"]