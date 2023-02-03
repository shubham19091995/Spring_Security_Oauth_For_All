FROM openjdk:11
EXPOSE 8080
ADD target/crudapp.jar crudapp.jar
ENTRYPOINT ["java","-jar","/crudapp.jar"]