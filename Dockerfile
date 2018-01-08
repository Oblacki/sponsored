FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./target/sponsored-2.5.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "sponsored-2.5.0-SNAPSHOT.jar"]