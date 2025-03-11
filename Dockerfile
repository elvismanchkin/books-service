FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create a non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy the JAR file
COPY target/reactive-books-service-*.jar app.jar

# Create necessary directories and set permissions
RUN mkdir -p /app/logs && \
    chown -R appuser:appgroup /app

# Switch to the non-root user
USER appuser

# Set default environment variables
ENV LOG_FORMAT=JSON
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0"

# Expose the application port
EXPOSE 8080

# Set health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
    CMD wget -q --spider http://localhost:8080/api/probes/liveness || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]