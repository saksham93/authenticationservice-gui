FROM openjdk:8-jre-alpine
COPY ./target/authenticationservice-gui-0.0.1-SNAPSHOT.jar /home/saksham/apps/
WORKDIR /home/saksham/apps/
CMD ["java", "-jar", "authenticationservice-gui-0.0.1-SNAPSHOT.jar","--server.port=8080"]