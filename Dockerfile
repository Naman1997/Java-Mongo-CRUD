# FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
FROM adoptopenjdk/openjdk13:ubi
COPY . /app
EXPOSE 8080
WORKDIR /app
CMD ./gradlew assemble
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar build/libs/crud-*-all.jar crud.jar