# Timemanager-frontend

-------------

[Vue.js](https://vuejs.org/) frontend application for [Timemanager](https://github.com/AdrianBCodes/timemanager) project.

## Features

- User authentication and authorization: Allowing users to create accounts, log in and log out, and authenticate requests to the API using JSON Web Tokens (JWT).
- Displaying data: Displaying user-friendly interface. Making requests to the API to read data.
- Search and filtering: Using query parameters to make requests to the API with the search or filter criteria.
- Create, update and delete data: Using forms to collect user input and making requests to the API to create, update or delete data.
- Error handling: Handling errors that may occur when making requests to the API, such as network errors, server errors, or validation errors.

## Built with

- [Vue.js](https://vuejs.org/)


## Project setup

### Using Docker

#### Clone the repository
```console
git clone https://github.com/AdrianBCodes/timemanager.git
```

#### Build Docker Image
```console
cd timemanager-frontend
docker build -t timemanager-frontend:1.0
```

#### Start Docker Container
```console
docker run -p 3000:3000 timemanager-frontend:1.0
```
This command will start the Docker container and expose port 3000 to the host system. You can change port number, if you want to expose it on a different one.

#### Access the application
```console
http://localhost:3000
```

Make sure you have Docker installed on your system before running the above commands.

### Without Docker

For building and running the application you need:
- [Node.js](https://nodejs.org)


#### Clone the repository
```console
git clone https://github.com/AdrianBCodes/timemanager.git
```

#### Install project dependencies
```
cd timemanager-frontend
npm install
```

#### Compiles and hot-reloads for development
```
npm run serve
```

#### Compiles and minifies for production
```
npm run build
```

#### Lints and fixes files
```
npm run lint
```
