FROM openjdk:21-jdk-slim AS build

WORKDIR /app
COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM openjdk:21-jdk-slim

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8082

CMD ["java", "-jar", "app.jar"]