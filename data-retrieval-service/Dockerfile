FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache curl
COPY target/data-retrieval-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9004

ENTRYPOINT ["java","-jar","app.jar"]