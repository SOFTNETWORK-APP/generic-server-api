import sbt.Resolver

import Common._
import app.softnetwork.sbt.build._

/////////////////////////////////
// Defaults
/////////////////////////////////

app.softnetwork.sbt.build.Publication.settings

/////////////////////////////////
// Useful aliases
/////////////////////////////////

addCommandAlias("cd", "project") // navigate the projects

addCommandAlias("cc", ";clean;compile") // clean and compile

addCommandAlias("pl", ";clean;publishLocal") // clean and publish locally

addCommandAlias("pr", ";clean;publish") // clean and publish globally

addCommandAlias("pld", ";clean;local:publishLocal;dockerComposeUp") // clean and publish/launch the docker environment

addCommandAlias("dct", ";dockerComposeTest") // navigate the projects

ThisBuild / shellPrompt := prompt

ThisBuild / organization := "app.softnetwork.api"

name := "generic-server-api"

ThisBuild / version := "0.2.6.1"

ThisBuild / scalaVersion := "2.12.15"

ThisBuild / scalacOptions ++= Seq("-deprecation", "-feature", "-target:jvm-1.8")

ThisBuild / javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

ThisBuild / resolvers ++= Seq(
  "Softnetwork Server" at "https://softnetwork.jfrog.io/artifactory/releases/",
  "Maven Central Server" at "https://repo1.maven.org/maven2",
  "Typesafe Server" at "https://repo.typesafe.com/typesafe/releases"
)

ThisBuild / versionScheme := Some("early-semver")

val scalatest = Seq(
  "org.scalatest" %% "scalatest" % Versions.scalatest  % Test
)

val akkaHttp: Seq[ModuleID] = Seq(
  "com.typesafe.akka" %% "akka-http" % Versions.akkaHttp,
  "com.typesafe.akka" %% "akka-http2-support" % Versions.akkaHttp, // required for akka-grpc
  "com.typesafe.akka" %% "akka-http-spray-json" % Versions.akkaHttp,
  "de.heikoseeberger" %% "akka-http-json4s" % Versions.akkaHttpJson4s
)

ThisBuild / libraryDependencies ++= Seq(
  "app.softnetwork.persistence" %% "persistence-core" % Versions.genericPersistence,
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.1"
) ++ scalatest ++ akkaHttp

Test / parallelExecution := false

lazy val core = project.in(file("."))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings, BuildInfoSettings.settings)
  .enablePlugins(BuildInfoPlugin)

lazy val testkit = project.in(file("testkit"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .enablePlugins(BuildInfoPlugin)
  .dependsOn(
    core % "compile->compile;test->test;it->it"
  )

lazy val root = project.in(file("."))
  .aggregate(core, testkit)
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
