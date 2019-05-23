FROM gradle:jdk11 gradle-build
RUN apt-get update && \
    apt-get install git-all -y
RUN git clone https://github.com/hardikgw/spring-boot-demo.git
RUN mkdir /app &&\
    cp -r /spring-boot-demo/* /app
RUN cd /app && mvn install

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=maven-build /app/target/*.jar /app

EXPOSE 8080
ENTRYPOINT java -jar demo-0.0.1-SNAPSHOT.jar
