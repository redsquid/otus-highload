FROM openjdk:17-alpine
COPY ./target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=docker", "/app/app.jar"]
