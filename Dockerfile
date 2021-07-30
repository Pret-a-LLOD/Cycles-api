FROM openjdk:11-jre-slim

EXPOSE 8080
USER root
WORKDIR /app

COPY ./ /app
RUN chmod +x /app

COPY ./src/main/resources/openapi.yml /openapi.yml

CMD ["java","-jar","/app/target/cycles-0.0.1-SNAPSHOT.jar"]
