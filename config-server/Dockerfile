FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache curl git
COPY target/config-server-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.yml /usr/app/

EXPOSE 8101

ENTRYPOINT ["java","-jar","app.jar", "--spring.config.location=file:/usr/app/application.yml"]