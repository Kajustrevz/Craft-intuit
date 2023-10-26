# Use a base image with Java 11 and a lightweight Linux distribution
FROM maven:3.8.1-openjdk-11

# Set the working directory within the container
WORKDIR /app

# Copy the pom.xml file to the container
COPY pom.xml .

# Copy the src directory to the container
COPY . .

# Build the application with Maven
RUN mvn clean install -DskipTests

# Expose port 8080 for the application
EXPOSE 8080

RUN pwd

RUN ls

# Run the application when the container starts
CMD ["java", "-jar", "/app/target/craft-app-0.0.1-SNAPSHOT.jar"]