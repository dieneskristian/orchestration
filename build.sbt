name := "orchestration"

version := "1.0"

mainClass in Compile := Some("Main")

scalaVersion := "2.12.4"

lazy val akkaVersion = "2.5.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % "2.5.6",
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.typesafe.akka" %% "akka-http-core" % "10.0.10",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.10",
  "com.typesafe.akka" % "akka-cluster-metrics_2.12" % "2.5.6",
  "ch.megard" %% "akka-http-cors" % "0.3.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.typesafe.akka"   %% "akka-persistence" % akkaVersion,
  "org.iq80.leveldb"  % "leveldb"           % "0.7",
  "org.fusesource.leveldbjni"  % "leveldbjni-all"    % "1.8"
)