FROM openjdk:21-jdk-slim

WORKDIR /app

COPY src/main/resources/Interns_2025_SWIFT_CODES.csv /app/main/resources/Interns_2025_SWIFT_CODES.csv
COPY target/Task-0.0.1-SNAPSHOT.jar remitly.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "remitly.jar"]