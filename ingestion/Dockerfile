FROM openjdk:8-jdk-alpine
MAINTAINER emiliano
VOLUME /tmp
EXPOSE 8080

ADD target/ingestion-1.0-SNAPSHOT.jar ingestion.jar

ADD src/main/resources /tmp/src/main/resources

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/ingestion.jar"]