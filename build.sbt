name := "AkkaAssignment-2"

version := "1.0"

scalaVersion := "2.11.8"
val akkaVersion = "2.4.17"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion
)
libraryDependencies += "log4j" % "log4j" % "1.2.17"