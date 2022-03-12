ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.1"

lazy val root = (project in file("."))
  .settings(
    name := "git-commit-analyzer",
    idePackagePrefix := Some("com.github.leopfeiffer.gitcommitanalyzer")
  )

lazy val doobieVersion = "1.0.0-RC1"

libraryDependencies ++= Seq(
  // Test
  "org.scalatest" %% "scalatest" % "3.2.11" % Test,

  // Web framework
  "com.lihaoyi" %% "cask" % "0.8.0",
  "com.lihaoyi" %% "requests" % "0.7.0",

  // Database handler
  "org.xerial" % "sqlite-jdbc" % "3.36.0.2",
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.tpolecat" %% "doobie-hikari" % doobieVersion,
  "org.tpolecat" %% "doobie-scalatest" % doobieVersion % Test,
)
