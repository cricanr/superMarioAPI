name := "superMarioPowerRestAPI"
 
version := "1.0" 
      
lazy val `supermariopowerrestapi` = (project in file(".")).enablePlugins(PlayScala)

      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.13.5"
val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "com.github.tototoshi" %% "scala-csv" % "1.3.10",
//  "io.circe" %% "circe-core" % circeVersion,
//  "io.circe" %% "circe-generic" % circeVersion,
//  "io.circe" %% "circe-parser" % circeVersion,
  jdbc , ehcache , ws , specs2 % Test , guice,
)
      