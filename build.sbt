// *****************************************************************************
// Projects
// *****************************************************************************

resolvers += Resolver.sonatypeRepo("releases")

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "terminology",
    libraryDependencies ++= Seq(
      // compiler plugins
      compilerPlugin(library.kindProjector),
      // compile time dependencies
      library.shapeless % Compile,
      // test dependencies
      library.scalaTest % "test",
      library.ammonite % "test"
    )
  )

addCommandAlias("example", ";test:run src/test/resources/example.sc")

// *****************************************************************************
// Dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val kindProjector = "0.9.6"
      val shapeless     = "2.3.3"
      val scalaTest     = "3.0.5"
      val ammonite      = "1.1.0"
    }
    val kindProjector   = "org.spire-math" %% "kind-projector" % Version.kindProjector
    val shapeless       = "com.chuusai"    %% "shapeless"      % Version.shapeless
    val scalaTest       = "org.scalatest"  %% "scalatest"      % Version.scalaTest
    val ammonite        = "com.lihaoyi"    %% "ammonite"       % Version.ammonite cross CrossVersion.full
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val commonSettings = Seq(
  scalaVersion := "2.12.4",
  organization := "co.upvest",
  scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation",
    "-language:_",
    "-target:jvm-1.8",
    "-encoding",
    "UTF-8",
    "-Xfatal-warnings",
    "-Ywarn-unused-import",
    "-Yno-adapted-args",
    "-Ywarn-inaccessible",
    "-Ywarn-dead-code",
    "-Ywarn-infer-any",
    "-Ywarn-nullary-override",
    "-Ywarn-nullary-unit",
    "-Ywarn-unused-import",
    "-Ypartial-unification",
    "-Xmacro-settings:materialize-derivations"
  ),
  scalacOptions in (Compile, console) ~= {
    _ filterNot (_ == "-Ywarn-unused-import")
  },
  javacOptions ++= Seq( "-source", "1.8", "-target", "1.8"),
  cancelable in Global := true,
  fork in Global := true
)
