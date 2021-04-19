# 為了mvn clean pacakge
FROM maven:3.6.3-adoptopenjdk-11 AS build
# Docker file creator
MAINTAINER b10709036@gapps.ntust.edu.tw

#FROM maven:3.6.3-jdk-11 AS build
COPY . /tmp
WORKDIR /tmp
RUN mvn -Dmaven.test.skip=true -Dmaven.javadoc.skip=true clean package -B -V -X
#Only Clean package
#RUN mvn clean package

# for rjava - jar run jar file
FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8082
RUN mkdir /opt/app

COPY --from=build /tmp/target/*.jar /opt/app/easycook.jar

ENTRYPOINT ["java", "-jar", "/opt/app/easycook.jar"]
