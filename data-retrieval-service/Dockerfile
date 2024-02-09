FROM maven:3-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk
COPY --from=build /target/data-analysis-service-0.0.1-SNAPSHOT.jar app.jar
COPY /src/main/resources/application.yml /usr/app/

EXPOSE 9001

ENTRYPOINT ["java","-jar","app.jar", "--spring.config.location=file:/usr/app/application.yml"]