FROM openjdk:8-alpine

RUN apk update && apk add bash

COPY target/*.jar ./app.jar

CMD [ "java", "-jar", "app.jar" ]