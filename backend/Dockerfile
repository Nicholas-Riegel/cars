# 🧰 1st Stage: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the project's pom.xml so we can cache dependencies
COPY pom.xml .

# Download dependencies before copying the source code
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Package the app — skip tests to make the build faster
RUN mvn package -DskipTests

# 🧰 2nd Stage: Run the application
FROM eclipse-temurin:21-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file from the 1st stage
COPY --from=build /app/target/*.jar app.jar

# Document the port your app will listen on
EXPOSE 8080

# Start the Spring Boot app
ENTRYPOINT ["java","-jar","app.jar"]
