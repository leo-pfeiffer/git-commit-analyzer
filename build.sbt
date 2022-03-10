ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.1"

lazy val root = (project in file("."))
  .settings(
    name := "git-commit-analyzer",
    idePackagePrefix := Some("com.github.leopfeiffer.gitcommitanalyzer")
  )

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.11" % Test,
  "com.lihaoyi" %% "cask" % "0.8.0"
)
