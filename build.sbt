// *****************************************************************************
// Projects
// *****************************************************************************

resolvers += Resolver.sonatypeRepo("releases")

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
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

// *****************************************************************************
// Release settings
// *****************************************************************************

organization in ThisBuild := "co.upvest"

lazy val tagName = Def.setting{
  s"v${if (releaseUseGlobalVersion.value) (version in ThisBuild).value else version.value}"
}

lazy val credentialSettings = Seq(
  credentials ++= (for {
    username <- Option(System.getenv().get("SONATYPE_USERNAME"))
    password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
)

import sbtrelease.ReleaseStateTransformations._
import sbtrelease.Version

lazy val releaseSettings = Seq(
  releaseTagName := tagName.value,
  pgpReadOnly := true,
  pgpSigningKey := Some(3042900655697024585L),
  pgpPassphrase := Some(Array.empty),
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  releaseVcsSign := true,
  releaseVersionBump := Version.Bump.Minor,
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := Function.const(false),
  releaseCommitMessage := s"Bumping version\n\n[skip ci]",
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("Snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("Releases" at nexus + "service/local/staging/deploy/maven2")
  },
  publishConfiguration := publishConfiguration.value.withOverwrite(isSnapshot.value),
  PgpKeys.publishSignedConfiguration := PgpKeys.publishSignedConfiguration.value.withOverwrite(isSnapshot.value),
  publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(isSnapshot.value),
  PgpKeys.publishLocalSignedConfiguration := PgpKeys.publishLocalSignedConfiguration.value.withOverwrite(isSnapshot.value),
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    setNextVersion,
    commitNextVersion,
    releaseStepCommand("sonatypeReleaseAll"),
    pushChanges
  )
)

lazy val publishSettings = Seq(
  homepage := Some(url("https://github.com/toknapp/terminology")),
  licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/toknapp/terminology"),
      "scm:git@github.com:toknapp/terminology.git")
  ),
  pomExtra := (
    <developers>
      <developer>
        <id>allquantor</id>
        <name>Ivan Morozov</name>
        <url>https://github.com/allquantor/</url>
      </developer>
      <developer>
        <id>rootmos</id>
        <name>Gustav Behm</name>
        <url>https://github.com/rootmos/</url>
      </developer>
    </developers>
    )
) ++ credentialSettings ++ releaseSettings
