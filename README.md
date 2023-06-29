A simple Quote Application with the following API:

    1. Create a quote.
    2. Update a quote.
    3. Get a quote with a specific ID.
    4. Delete a quote with a specific ID.
    5. Get a random quote.
    6. Get all quotes.
    7. Get quotes that contain specific text (e.g. "discover").

It is implemented using Java 11 and Spring Boot, MySql as the database and
the entire application can be executed in a docker environment using the 
**docker-compose.yml** file.

In order to execute the whole application run these commands
```bash
# in order to create the jar
## for windows
.\mvn.cmd clean package 
## or for linux/mac: ./mvn clean package

# in order to build the docker image for the spring boot app
docker-compose build 

# start the both mysql and quotes app containers 
docker-compose up
```