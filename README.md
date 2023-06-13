### Coding Challenge

The goal of the challenge is to create a very simple Spring Boot application for managing charge points used to charge electric vehicles.
Requirement.md contains detailed explanation of the goals and behavior of application.

To run application in the host environment, use command:

```
./gradlew bootRun
```

To run application in the docker container, use command:

```
./gradlew dockerRun
```

Use root URL to access Swagger UI:

```
http://localhost:8080/
```

Api understands base64 auth headers, default credentials:

```
admin admin
user user
```
