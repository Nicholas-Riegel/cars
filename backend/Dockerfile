# 🧰 1st Stage: Build with resource limits
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Set Maven options for minimal resource usage
ENV MAVEN_OPTS="-Xmx512m -XX:MaxMetaspaceSize=256m"

# Set the working directory inside the container
WORKDIR /app

# Copy the project's pom.xml for better layer caching
COPY pom.xml .

# Download dependencies with optimizations
RUN mvn dependency:go-offline -B --quiet --no-transfer-progress

# Copy only source code (uploads excluded via .dockerignore)
COPY src ./src

# Package with optimizations - skip tests, minimize output, limit memory
RUN mvn package -DskipTests --quiet --no-transfer-progress \
    -Dspring-boot.run.jvmArguments="-Xmx256m"

# 🧰 2nd Stage: Minimal runtime image
FROM eclipse-temurin:21-jre-alpine

# Create non-root user for security and lower resource usage
RUN addgroup -g 1000 spring && adduser -u 1000 -G spring -s /bin/sh -D spring

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file from the 1st stage
COPY --from=build /app/target/*.jar app.jar

# Create uploads directory with proper permissions
RUN mkdir -p uploads && chown -R spring:spring /app

# Switch to non-root user
USER spring

# Document the port your app will listen on
EXPOSE 8080

# Start with JVM optimizations for low-resource environments
ENTRYPOINT ["java", \
    "-Xmx384m", \
    "-Xms128m", \
    "-XX:MaxMetaspaceSize=128m", \
    "-XX:+UseG1GC", \
    "-XX:+UseStringDeduplication", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", \
    "app.jar"]
