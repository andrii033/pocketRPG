FROM openjdk:17-jdk-slim
LABEL authors="atara"

COPY gradle/wrapper/gradle-wrapper.jar /app/gradle-wrapper.jar

ENTRYPOINT ["java", "-jar", "/app/gradle-wrapper.jar"]
