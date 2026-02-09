FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache curl
COPY target/auth-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9002

ENTRYPOINT ["java","-jar","app.jar"]