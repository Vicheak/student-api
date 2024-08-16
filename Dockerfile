FROM openjdk:17 AS build

COPY target/student-management-1.0.0.jar student-management-1.0.0.jar

ENTRYPOINT ["java", "-jar", "/student-management-1.0.0.jar"]