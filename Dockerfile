FROM maven:3.6.3-adoptopenjdk-11 AS build
COPY pom.xml /tmp/pom.xml
RUN mvn -B -f /tmp/pom.xml -s /usr/share/maven/ref/settings-docker.xml dependency:resolve

FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8082
RUN mkdir /opt/app

COPY --from=build tmp/target/*.jar /opt/app/kioxia.jar

ENTRYPOINT ["java", "-jar", "/opt/app/kioxia.jar"]
