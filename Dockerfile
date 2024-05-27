FROM openjdk:16-alpine
WORKDIR /
COPY jigang-farm.jar jigang-farm.jar
EXPOSE 8080
CMD ["java","-jar","jigang-farm.jar"]