# Application 
Application is developed in Scala 13 as a Play Framework REST API application. 
As a datasource it uses 2 CSV files for reading speed & powerness data for Super Mario characters.

# Running application
## Using sbt
Prerequisites:
- install java, minimum JDK 8 or ofc newer version
- install sbt: https://www.scala-sbt.org/1.x/docs/Setup.html
  Sbt is the industry standard used Scala build tool

Compile test & run application locally:
Open sbt in the repo root folder:
Compile & run tests:

`sbt clean compile test`

Run application:
```sbt run```


# Architecture & code notes:
The application is a REST API architecture developed in Scala using PlayFramework. The components are: 
* CSV Parser - data layer
* a Service class - business logic layer
* data models - transport layer objects
* the controller class which defines the REST API interface and calls the service that will trigger the business logic, data layer operations

# API Specifications:
1) Get all character names unsorted:
HTTP method: GET

URL: `http://localhost:9000/names`

BODY: 

Response: 


2) Get all characters sorted (asc or desc):
HTTP method: GET

URL: `http://localhost:9000/charactersSorted?sortOrder=asc`

BODY:

Response:


3) Search characters:
HTTP method: POST (for simplicity to use rather then GET)

URL: `http://localhost:9000/search`

BODY: `{"names": ["Aerodent"]}`

Response: 


4) Create new character: 
HTTP method: POST

URL: `http://localhost:9000/create`

BODY: `{"name": "test", "firstGame": "test1", "power": 232.1, "speed": 123}`

Response: 


5) Update existing character: 
HTTP method: POST

URL: `http://localhost:9000/create`

BODY: `{"name": "test", "firstGame": "test1", "power": 232.1, "speed": 123}`

Response: 


## Docker Version:
You can also create a docker image of the application and run it using docker
Prerequisites:
Install docker: https://docs.docker.com/get-docker/

Create docker image locally

```sbt docker:publishLocal```

NOTE: I get some errors while publishing but still it finishes successfully and the setup works. Would need to investigate
further those errors. 

Check images:
```docker images -a```
Output example:
```
➜  superMarioPowerRestAPI git:(master) docker images -a
REPOSITORY                                                  TAG                         IMAGE ID       CREATED             SIZE
supermariopowerrestapi                                      1.0                         68e88b5ffd62   24 minutes ago      588MB
```

Running docker image created (`superMarioPowerRestAPI`):

```docker run supermariopowerrestapi:1.0 -Dplay.http.secret.key='your_key'```
where the application secret key can be any key you want, more details here: 
https://www.playframework.com/documentation/2.8.x/ApplicationSecret
You can use any string as a secret key. 

And then you can do HTTP requests using Postman. For convenience here is also a POSTMAN project prepared: 
`Postman Project/SuperMarioAPI.postman_collection.json`
For installing Postman locally check this link: https://www.postman.com/downloads/


# Further development:
As a minimal solution I have not considered for now some points that should be addressed:
* error handling, this is definitely needed whenever we do IO operations, e.g. the CSV parser class and then any error should bubble up and be handled in the REST API
* more test cases especially for negative cases not treated due to lack of error handling in this version
* data validation, currently this is missing
* running on local machine without any scaling and optimization is definitely not going to perform well in a real prod requirements environment. In a real env we should consider having a cluster of REST API instances and more instances of the IO level
* logging should be added to our application
* add a log injection mechanism to collect logs for further monitoring
* add configuration entries
* add CI/CD
* add API documentation using Swagger API: https://swagger.io/


# Scalafmt:

In order to have well formatted, consistent, easy to maintain code approved by Scala community
standards I use Scalafmt. It is configurable to work within IntelliJ or other IDEs, integrated with your favourite shortcuts
and also at build time when a file is saved code will be reformatted accordingly.
Installation documentation: https://scalameta.org/scalafmt/

Useful sbt commands to run Scalafmt tasks:

```
scalafmt
scalafmtAll
scalafmtCheck
scalafmtCheckAll
scalafmtDoFormatOnCompile
scalafmtOnly
scalafmtSbt
scalafmtSbtCheck
```


# Useful links:
1. Playframework: https://www.playframework.com/
2. Scalatest for creating tests: https://www.scalatest.org/
3. Scalafmt: https://scalameta.org/scalafmt/
4. Scala CSV Parser: https://github.com/tototoshi/scala-csv
5. Guice dependency injection for Scala: https://github.com/codingwell/scala-guice
6. Docker: https://www.docker.com/
