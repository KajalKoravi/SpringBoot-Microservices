# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

#information around who maintains the image
MAINTAINER kajalkoravi

#Copy the application JAR file into the container
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

#execute the application
ENTRYPOINT ["java","-jar","accounts-0.0.1-SNAPSHOT.jar"]



