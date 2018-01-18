name := "klarna-fp"

scalaVersion := "2.12.4"

val http4sVersion = "0.18.0-M8"
val fs2Version = "0.10.0-M11"
val scalatestVersion = "3.0.3"
val catsVersion = "1.0.1"
val catsEffectVersion = "0.7"
val circeVersion = "0.9.0"
val disciplineVersion = "0.8"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.http4s"    %% "http4s-dsl"          % http4sVersion,
  "org.http4s"    %% "http4s-blaze-server" % http4sVersion,
  "org.http4s"    %% "http4s-blaze-client" % http4sVersion,
  "co.fs2"        %% "fs2-core"            % fs2Version,
  "co.fs2"        %% "fs2-io"              % fs2Version,
  "org.typelevel" %% "cats-core"           % catsVersion,
  "org.typelevel" %% "cats-effect"         % catsEffectVersion,
  "org.typelevel" %% "cats-free"           % catsVersion,
  "org.typelevel" %% "discipline"          % disciplineVersion,
  "io.circe"      %% "circe-core"          % circeVersion,
  "io.circe"      %% "circe-parser"        % circeVersion,
  "io.circe"      %% "circe-generic"       % circeVersion,
)

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % scalatestVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion,
  "org.typelevel" %% "cats-laws" % catsVersion,
  "org.typelevel" %% "cats-testkit" % catsVersion,
  "com.github.alexarchambault" %% "scalacheck-shapeless_1.13" % "1.1.6",
).map(_ % "test")

scalacOptions ++= Seq(
  "-feature",
  "-language:higherKinds",
  "-Ypartial-unification",
)

scalaSource in Compile := baseDirectory.value / "src"
scalaSource in Test    := baseDirectory.value / "test"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")
