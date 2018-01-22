FROM openjdk:8-alpine
ADD ./target/greeting-0.0.1-SNAPSHOT.jar /greeting.jar
RUN sh -c 'touch /greeting.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/greeting.jar"]
