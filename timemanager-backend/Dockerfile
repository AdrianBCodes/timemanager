FROM maven:3.8.6-openjdk-18 AS build
COPY src /home/timemanager-backend/src
COPY pom.xml /home/timemanager-backend
RUN mvn -f /home/timemanager-backend/pom.xml clean package -Dmaven.test.skip

FROM openjdk:18
COPY --from=build /home/timemanager-backend/target/timemanager-backend.jar /usr/local/lib/timemanager-backend.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/timemanager-backend.jar"]