# Use Temurin 17 JDK (Ubuntu base, multi-arch)
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy JAR
COPY target/metar-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java","-jar","app.jar"]

