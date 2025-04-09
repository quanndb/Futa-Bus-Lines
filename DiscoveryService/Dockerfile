##FROM maven:3.9.6-eclipse-temurin-21 AS build
##
##WORKDIR /app
##
##COPY . .
##
##RUN mvn clean package -DskipTests
#
#FROM eclipse-temurin:21-jre-ubi9-minimal
#
#WORKDIR /app
#
#COPY --from=build /app/target/*.jar /app/discovery-service.jar
#
#EXPOSE 8761
#
#ENTRYPOINT ["java","-jar","discovery-service.jar"]

FROM eclipse-temurin:21-jre-ubi9-minimal

# Set the current working directory inside the image
WORKDIR /app

COPY ./target/*.jar /app/app.jar
ADD ./src/main/resources /app/config

EXPOSE 8082

ENTRYPOINT ["java","-jar","app.jar","--spring.profiles.active=dev"]