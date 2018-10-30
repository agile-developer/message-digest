##### Instructions:
* This project can be run with Java SE 8 or upwards, and Maven 3.5.x.
* Run `mvn clean package` to build the project. This will produce a jar file in the `target` directory.
* Run `java -jar target/message-digest-0.0.1-SNAPSHOT.jar` to run the application.
* Use `curl` or Postman to issue POST or GET commands to `http:\\localhost:8080\messages`.

##### Usage:
* This is an example POST request using `curl` to get a SHA256 digest:  
`curl -i -w '\n' -X POST -H "Content-Type: application/json" -d '{"message" : "foo"}' http://localhost:8080/messages`
* This is an example GET request using curl to retrieve the message for a previously generated digest:  
`curl -i -w '\n' http://localhost:8080/messages/2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae`
* Attempting to retrieve a message for an unknown digest will result in an `HTTP 404 Not Found`.
