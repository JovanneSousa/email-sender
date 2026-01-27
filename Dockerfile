FROM maven:3.9.2-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jdk-alpine

WORKDIR /app

COPY target/email-sender.jar ./email-sender.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "email-sender.jar"]
