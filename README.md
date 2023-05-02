# Timemanager

-------------

## Features

- User authentication and authorization: Allowing users to create accounts, log in and log out, and verify requests to the API using JSON Web Tokens (JWT).
- Displaying data: Allowing users to view and sort data tables for core application's elements.
- Search and filtering: Allowing users to search and filter data displayed on the page.
- Create, update and delete data: Allowing users to create new data and update existing data.
- Error handling: Handling errors that may occur when making requests to the API, such as network errors, server errors, or validation errors.

## Built with

- Vue.js
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

#### Use Docker Compose
```console
docker-compose up -d
```

You can check status of application's stack
```console
docker-compose ps
```

#### Access the application
```console
http://localhost:3000
```

Make sure you have Docker installed on your system before running the above commands.

### Without Docker

In order to access full application you need to setup:
- timemanager-frontend
- timemanager-backend

Please visit [timemanager-frontend README](https://github.com/AdrianBCodes/timemanager/blob/master/timemanager-frontend/README.md) and [timemanager-backend README](https://github.com/AdrianBCodes/timemanager/blob/master/timemanager-backend/README.md) for full details on how to do that.