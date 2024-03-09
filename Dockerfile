FROM ubuntu:latest
LABEL authors="vikas"

ENTRYPOINT ["top", "-b"]

FROM openjdk:19

WORKDIR /app

COPY target/RestaurantSystemKPO-1.0-SNAPSHOT.jar /app/RestaurantSystemKPO-1.0-SNAPSHOT.jar

CMD ["wait-for-it", "db:3306", "--", "java", "-jar", "RestaurantSystemKPO-1.0-SNAPSHOT.jar"]