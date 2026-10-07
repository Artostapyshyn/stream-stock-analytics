FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache curl
COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9003 9013

ENTRYPOINT ["java", \
    "-Xms512m", \
    "-Xmx1024m", \
    "-XX:MaxDirectMemorySize=512m", \
    "-XX:+UseZGC", \
    "-XX:+ZGenerational", \
    "-XX:StartFlightRecording=disk=true,dumponexit=true,filename=/tmp/recording.jfr,maxage=5m,settings=profile", \
    "-Dcom.sun.management.jmxremote=true", \
    "-Dcom.sun.management.jmxremote.port=9013", \
    "-Dcom.sun.management.jmxremote.rmi.port=9013", \
    "-Dcom.sun.management.jmxremote.local.only=false", \
    "-Dcom.sun.management.jmxremote.authenticate=false", \
    "-Dcom.sun.management.jmxremote.ssl=false", \
    "-Djava.rmi.server.hostname=localhost", \
    "-jar", "app.jar"]
