FROM eclipse-temurin:21-jdk
COPY target/discovery-server-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8001

ENTRYPOINT ["java","-jar","app.jar"]