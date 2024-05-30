FROM openjdk:16-alpine
WORKDIR /
COPY target/Jingang-Farm-Management-0.0.1-SNAPSHOT.jar Jingang-Farm-Management-0.0.1-SNAPSHOT.jar
EXPOSE 8081
CMD ["java", "-jar", "Jingang-Farm-Management-0.0.1-SNAPSHOT.jar"]
