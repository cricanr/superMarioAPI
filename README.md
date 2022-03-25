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


#Architecture & code notes:

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

* logging should be added to our application
* add a log injection mechanism to collect logs for further monitoring
* add configuration entries
* add CI/CD


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