# Quote Application API Documentation

In Quote Application, the operations that are available are create, update,
retrieve and delete quotes. Additionally, random quotes can be retrieved, all 
quotes using pagination and search for quotes containing specific text.

## General Information
The project is implemented using Java 17, Spring Boot and Spring Security. 
Databases that have been used are MySql for storing and Redis for caching.
The application can be executed in a docker environment using the 
**docker-compose.yml** file.

## Execution commands

Simply execute `docker-compose up --build`.

[//]: # (```bash)

[//]: # (# in order to create the jar)

[//]: # (## for windows)

[//]: # (.\mvn.cmd clean package -DskipTests)

[//]: # (## or for linux/mac: ./mvn clean package)

[//]: # ()
[//]: # (# in order to build the docker image for the spring boot app)

[//]: # (docker-compose build )

[//]: # ()
[//]: # (# start the both mysql and quotes app containers )

[//]: # (docker-compose up)

[//]: # (```)

## API
After starting the application, visit this [swagger link](http://localhost:8080/swagger-ui/index.html#/)
in order to explore the available API endpoints.

### Notes
* In directory `src/main/scripts` the file **/mysql-setup.sql** was created
in order to create *quotes_db* database.
* In directory `src/main/resources/db/migration` exist the sql scripts that 
handle the tables creation.
* A postman collection and an postman environment are included, **Quotes.postma_collection.json**
and **Quotes.postman_environment.json** respectively.