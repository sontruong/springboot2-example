FROM tomcat:jdk12-adoptopenjdk-openj9
VOLUME /tmp
ARG JAR_FILE=target/war-docker-0.0.1.war
ADD ${JAR_FILE} /usr/local/tomcat/webapps
