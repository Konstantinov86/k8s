FROM openjdk:17-slim
EXPOSE 8091
COPY target/subject-service-*.jar /opt/app/subject-service.jar
WORKDIR /opt/app
ENTRYPOINT exec java $JAVA_OPTS -jar /opt/app/subject-service.jar
