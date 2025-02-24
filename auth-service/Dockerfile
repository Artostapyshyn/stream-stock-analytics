FROM maven:3-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk
COPY --from=build /target/auth-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9002

ENTRYPOINT ["java","-jar","app.jar"]