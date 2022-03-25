
name := "superMarioPowerRestAPI"
 
version := "1.0" 
      
lazy val `supermariopowerrestapi` = (project in file(".")).enablePlugins(PlayScala)

      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "com.github.tototoshi" %% "scala-csv" % "1.3.10",
  "net.codingwell" %% "scala-guice" % "5.0.2",
  "org.scalatestplus" %% "mockito-3-12" % "3.2.10.0" % "test",
  "org.scalatest" %% "scalatest" % "3.2.11" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % "test",
    jdbc , ehcache , ws , specs2 % Test , guice,
)

mappings in Universal += file("app/resources/super-mario-characters-power.csv") -> "/app/resources/super-mario-characters-power.csv"
mappings in Universal += file("app/resources/super-mario-characters-speed.csv") -> "/app/resources/super-mario-characters-speed.csv"

import com.typesafe.sbt.packager.docker.DockerChmodType
import com.typesafe.sbt.packager.docker.DockerPermissionStrategy
dockerChmodType := DockerChmodType.UserGroupWriteExecute
dockerPermissionStrategy := DockerPermissionStrategy.CopyChown
