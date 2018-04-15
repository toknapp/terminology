// *****************************************************************************
// Projects
// *****************************************************************************

lazy val types = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "martind-types",
    libraryDependencies ++= Seq(
      // compiler plugins
      compilerPlugin(library.kindProjector),
      library.catsCore % Compile,
      library.catsFree % Compile,
      library.shapeless % Compile
    )
  )

// *****************************************************************************
// Dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val cats          = "1.1.0"
      val kindProjector = "0.9.6"
      val shapeless     = "2.3.3"
    }
    val catsCore        = "org.typelevel"  %% "cats-core"      % Version.cats
    val catsFree        = "org.typelevel"  %% "cats-free"      % Version.cats
    val kindProjector   = "org.spire-math" %% "kind-projector" % Version.kindProjector
    val shapeless       = "com.chuusai"    %% "shapeless"      % Version.shapeless
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val commonSettings = Seq(
  scalaVersion := "2.12.5",
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
  javaOptions ++= Seq(
    "-Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager"
  ),
  cancelable in Global := true,
  fork in Global := true
)
