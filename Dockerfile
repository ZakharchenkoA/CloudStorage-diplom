FROM openjdk:17-alpine
EXPOSE 8090
ADD target/CloudStorage-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]