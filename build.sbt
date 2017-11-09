name := "klarna-fp"

scalaVersion := "2.12.4"

val http4sVersion = "0.17.4"
val fs2Version = "0.9.7"
val scalatestVersion = "3.0.1"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl"          % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "co.fs2"     %% "fs2-core"            % fs2Version,
  "co.fs2"     %% "fs2-io"              % fs2Version,
)

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % scalatestVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion,
).map(_ % "test")

scalacOptions ++= Seq(
  "-feature",
  "-language:higherKinds",
  "-Ypartial-unification",
)

scalaSource in Compile := baseDirectory.value / "src"
scalaSource in Test    := baseDirectory.value / "test"
