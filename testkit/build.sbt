import app.softnetwork.sbt.build.Versions

Test / parallelExecution := false

organization := "app.softnetwork.api"

name := "generic-server-api-testkit"

val akkaHttpTestkit: Seq[ModuleID] = Seq(
  "app.softnetwork.persistence" %% "persistence-core-testkit" % Versions.genericPersistence,
  "com.typesafe.akka" %% "akka-http-testkit" % Versions.akkaHttp
)

libraryDependencies ++= akkaHttpTestkit
