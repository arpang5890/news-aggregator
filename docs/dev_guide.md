# Developer's guide

### Prerequisites:

- java 17
- Maven 3.x
- Docker (testing)

### Build and test

```shell
mvn clean verify
```

### What's used

#### Lombok

[Lombok](https://projectlombok.org/). To view the source code in your IDE, you need to install a
plugin that recognizes the Lombok annotations. To configure your IDE, follow the appropriate setup
link found
[here](https://projectlombok.org/setup/overview).

#### Code formatting and checkstyle

Use google code formatter / google checkstyle.
It is recommended to use https://plugins.jetbrains.com/plugin/8527-google-java-format formatter

### Running locally

#### Run Mysql

You can run Mysql in any convenient way, we also provide with docker-compose file with basic mysql
container setup.
Run mysql db locally via docker compose. Go to {projectDirectory}/docker/

```bash
docker-compose up
```

#### Run news aggregator application using intellij or  maven command

Either use intellij runner, or use maven command (from project root):

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=default
``` 

#### Run news aggregator application using docker

```bash
#build docker image
docker build -t news_aggregator -f docker/Dockerfile .
# Run application usign docker
docker run -d --name news_aggregator -p 8080:8080 news_aggregator
# check logs
docker logs -f news_aggregator
# stop application
docker stop news_aggregator
# or stop using ps
docker ps
docker stop <container_id_or_name>

### Integration tests

By default, the basic maven build runs all unit tests and IT-s.
If, for some reason, run the build without integration tests (using standard failsafe plugin
property):

```bash
mvn clean verify -DskipITs
```


### Useful URLs
- **Healthcheck:** [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- **Swagger Url:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Other Important Details
- **APIs:** Postman folder contains the collection - `News Item APIs.postman_collection.json` to access all the APIs.
    - **API security:** No API security is implemented as of now.
    - **API Specification:** We use OpenAPI 3 specifications for managing API specifications.
- **Database Migration:** We use Flyway DB migration scripts to manage DDL/DML. You can find these scripts in the `resources/db/migration` directory, organized by version.
- **To Add a New News Publisher:** To add a new news publisher, pleasse Refer to `V2__inserts_news_publishers.sql` for an example.
