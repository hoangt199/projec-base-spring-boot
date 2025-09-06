FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

# Copy Maven wrapper files with proper permissions
COPY mvnw ./mvnw
RUN chmod +x ./mvnw
COPY .mvn ./.mvn
COPY pom.xml .

# Copy all module pom files first
COPY common-library/pom.xml common-library/pom.xml
COPY eureka-server/pom.xml eureka-server/pom.xml
COPY config-service/pom.xml config-service/pom.xml
COPY gateway-service/pom.xml gateway-service/pom.xml
COPY user-service/pom.xml user-service/pom.xml
COPY notification-service/pom.xml notification-service/pom.xml

# Copy source code for common-library and gateway-service
COPY common-library/src common-library/src
COPY gateway-service/src gateway-service/src
COPY lombok.config lombok.config
COPY gateway-service/lombok.config gateway-service/lombok.config

# Use Maven directly instead of wrapper
RUN apk add --no-cache maven

# Build gateway-service (will automatically build common-library first)
RUN mvn clean package -pl gateway-service -am -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Add a non-root user to run the application
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the built jar file from the build stage
COPY --from=build /workspace/app/gateway-service/target/*.jar app.jar

# Set the entrypoint to run the jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Expose the port the app runs on
EXPOSE 8080

# Add health check
HEALTHCHECK --interval=30s --timeout=30s --start-period=60s --retries=3 CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1