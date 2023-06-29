FROM openjdk:11

MAINTAINER george.lykoudis@yahoo.com

COPY target/quotes-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]