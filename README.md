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