FROM openjdk:14-ea-12-jdk-oracle
VOLUME /tmp
ARG JAR_FILE=target/jar-docker-0.0.1.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]