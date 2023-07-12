## Retrospective: For those of you who are looking test task solution for NetValue (New Zealand)

* This task has been marked as intermediate level with tendency to hacker-style code.

* Actually task has been written following YAGNI principle with minimum effort, keeping in mind this won't be developed more after submission. So there are some simplifications made like returning entities for admin account from controller.

* Program is written around UseCase class which holds business logic, for such a small task I don't see any reason to put everything in separate folders following 3-layer architecture, anyway app has layers centered around use case, which could be deveoped further in hexagonal

* No unneeded interfaces, every line of code does what it's been required, no bullshiting around bloating simple application with interfaces or factories of factories.
  
* Program heavily focuses on business logic corner-cases, security and who can see what. Although it was not count, since insecure applications bloated with enterprise s###t got "senior work" labels.

## Coding Challenge

The goal of the challenge is to create a very simple Spring Boot application for managing charge points used to charge electric vehicles.

File **requirements.md** contains detailed explanation of the goals and behavior of application.

## Getting application up and running

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

API understands BASE64 auth headers, default credentials:

```
admin admin
user user
```
