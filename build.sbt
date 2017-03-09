name := """knock"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.opencsv" % "opencsv" % "3.9",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

