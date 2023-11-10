FROM openjdk:11-jre-slim

# Install the `procps` package, which includes the `ps` command
RUN apt-get update && apt-get install -y procps

# Set the working directory within the container
WORKDIR /app

COPY target/craft-app-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose port 8080 for the application
EXPOSE 8080

ENV ENVIRONMENT=prod

CMD ["java", "-jar", "app.jar"]
