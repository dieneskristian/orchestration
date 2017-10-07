name := "orchestration"

version := "1.0"

scalaVersion := "2.12.2"

lazy val akkaVersion = "2.5.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % "2.5.4",
  "com.typesafe.akka" % "akka-cluster-metrics_2.12" % "2.5.6",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)