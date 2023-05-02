# Timemanager-backend

-------------

Java and Spring boot REST API for [Timemanager](https://github.com/AdrianBCodes/timemanager) project.

## Features
- User authentication and authorization using JSON Web Tokens ([JWT](https://jwt.io)).
- Create, read, update or delete database records for core application's elements (Projects, Clients, Tasks, Tags, Tracker Events).
- Pagination, sorting and filtering of large result sets.
- Unit testing with [JUnit](https://junit.org/junit5/).


## Built with

- Java
- Maven
- Spring JPA
- Spring Web
- PostgreSQL

## Project setup

### Using Docker

#### Clone the repository
```console
git clone https://github.com/AdrianBCodes/timemanager.git
```

#### Build Docker Image
```console
cd timemanager-backend
docker build -t timemanager-backend:1.0
```

#### Start Docker Container
```console
docker run -p 8080:8080 timemanager-backend:1.0
```
This command will start the Docker container and expose port 8080 to the host system. You can change port number, if you want to expose it on a different one.

#### Access the application
```console
http://localhost:8080
```

Make sure you have Docker installed on your system before running the above commands.

### Without Docker

For building and running the application you need:

- [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or above
- [Maven 3](https://maven.apache.org)
- [PostgreSQL 14.6](https://www.postgresql.org)

Be aware that you need to locally setup PostgreSQL database named "timemanager" on port 5432 or configure `timemanager-backend/src/main/resources/application.properties` for your own database.

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.adrianbcodes.timemanager.TimemanagerApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```
