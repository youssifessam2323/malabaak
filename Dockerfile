# the base image
FROM openjdk:21-jdk

#  the JAR file path
ARG JAR_FILE=target/*.jar

RUN echo "we are running some # of cool thing"


# Copy the JAR file from the build context into the Docker image
COPY ${JAR_FILE} application.jar

# defining the application environment
ENV DB_HOST=host.docker.internal
ENV DB_NAME=malabaak
ENV DB_USER=malabaak
ENV DB_PASSWORD=mysecretpassword
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://$DB_HOST:5432/$DB_NAME
ENV DB_NAME=malabaak

CMD apt-get update -y

# Set the default command to run the Java application
ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/application.jar"]