# todo-app

This is a quick guide to start the todo-app on local

## Build

In the project root directory to install, run the tests and to create the jar file within the target folder, run:

```
mvn clean install
```

## Run the app

Firstly, the app requires a JWT secret key. After generating the key, put the value into `application.properties` file for `jwt.secret_key`.
Also the JWT expiry duration can be changed with `jwt.expire_duration`. (Default is 30mins)

### Run the app with docker-compose

If the app is desired to run with the docker-compose, all it needs to be done is to run:

```
docker-compose up
```
It will start the app and connect with mysql which also runs on docker.

### Run the app without docker-compose

If the app is to run with local db or any other db, then the commented database properties in `application.properties` file should be uncommented
and the database information should be filled. After database information is there simply can run the command:

```
mvn spring-boot:run
```

## Test the app

In order to check liveleness of the app just make this `curl` request and the response should be `"OK"`:
```
curl --location 'http://localhost:8005/auth/healthcheck'
```

## API Documentation

After running the app, the API documentation can be found => `http://localhost:8005/swagger-ui/index.html`
