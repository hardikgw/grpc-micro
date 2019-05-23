FROM gradle:jdk11 AS gradle-build
USER root
RUN mkdir /app
ADD . /app/.
RUN cd /app && gradle build

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=gradle-build /app/build/libs/*.jar /app

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "grpc-micro-0.1.0.jar"]
