name := """play-location"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  //webjars
 "org.webjars" %% "webjars-play" % "2.3.0",
 "org.webjars" % "bootstrap" % "3.1.1-2",
 // Slick
  "com.typesafe.play" %% "play-jdbc" % "2.3.1",
  "com.typesafe.play" %% "play-json" % "2.3.1",
  "com.typesafe.play" %% "play-slick" % "0.8.0-RC2",
  "com.github.tminglei" %% "slick-pg" % "0.6.0-M2",
  "com.github.tminglei" %% "slick-pg_joda-time" % "0.6.0-M2",
  "com.github.tminglei" %% "slick-pg_play-json" % "0.6.0-M2",
  "com.github.tminglei" %% "slick-pg_jts" % "0.6.0-M2",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "joda-time" % "joda-time" % "2.3",
  "org.joda" % "joda-convert" % "1.5",
  "com.vividsolutions" % "jts" % "1.13",
  "org.virtuslab" %% "unicorn-play" % "0.6.0-M8"
)
