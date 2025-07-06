FROM openjdk:17

LABEL authors="hyeon-young"

WORKDIR /app

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]