# Quote Application API Documentation

In Quote Application, the operations that are available are create, update,
retrieve and delete quotes. Additionally, random quotes can be retrieved, all 
quotes using pagination and search for quotes containing specific text.

## Base URL
The base URL for all endpoints is `http://localhost:8080`.  

## API Endpoints
The prefix for all endpoints is `/api/v1/quotes`.

## General Information
It is implemented using Java 11 and Spring Boot, MySql as the database and
the entire application can be executed in a docker environment using the 
**docker-compose.yml** file.

### Execution commands
```bash
# in order to create the jar
## for windows
.\mvn.cmd clean package -DskipTests
## or for linux/mac: ./mvn clean package

# in order to build the docker image for the spring boot app
docker-compose build 

# start the both mysql and quotes app containers 
docker-compose up
```

### Notes
* In directory `./src/main/scripts` the file **/mysql-setup.sql** was created
in order to create *quotes* database and *quote* table.
Also, 4 dummy quotes have been inserted with the respected ids 1,2,3 and 4. 

* A postman collection is included called **Quotes.postma_collection.json**.