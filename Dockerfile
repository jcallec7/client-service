FROM ubuntu:latest
LABEL authors="jcall"

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/client-0.0.1-SNAPSHOT.jar /app/client.jar

EXPOSE 7071

ENTRYPOINT ["java", "-jar", "client.jar"]
