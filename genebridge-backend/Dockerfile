# Stage 1: Build the app using a Maven-ready image
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the actual running image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar from the build stage
# Note: Ensure the filename matches what Maven generates in /target/
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
