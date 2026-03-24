FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=target/PruebaChakray-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} PruebaChakray-0.0.1-SNAPSHOT.jar
ENTRYPOINT [ "java", "-jar","PruebaChakray-0.0.1-SNAPSHOT.jar"]