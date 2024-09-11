FROM openjdk:17-jdk

COPY target/todoapp.jar .

EXPOSE 8005

ENTRYPOINT ["java", "-jar", "todoapp.jar"]