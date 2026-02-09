FROM eclipse-temurin:21-jdk
COPY target/data-analysis-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9000

ENTRYPOINT ["java","-jar","app.jar"]