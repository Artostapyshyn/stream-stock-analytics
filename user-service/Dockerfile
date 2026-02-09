FROM eclipse-temurin:21-jdk
COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9003

ENTRYPOINT ["java","-jar","app.jar"]