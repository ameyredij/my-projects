FROM openjdk:17

COPY target/*.jar online-bookstore-service.jar

ENTRYPOINT ["java","-jar","/online-bookstore-service.jar"]
