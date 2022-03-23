name := "superMarioPowerRestAPI"
 
version := "1.0" 
      
lazy val `supermariopowerrestapi` = (project in file(".")).enablePlugins(PlayScala)

      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "com.github.tototoshi" %% "scala-csv" % "1.3.10",
  "net.codingwell" %% "scala-guice" % "5.0.2",
  jdbc , ehcache , ws , specs2 % Test , guice,
)
