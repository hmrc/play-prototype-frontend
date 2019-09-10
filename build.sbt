import play.sbt.routes.RoutesKeys
import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings
import uk.gov.hmrc.{DefaultBuildSettings, SbtArtifactory}

val appName = "play-prototype-frontend"

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory)
  .settings(DefaultBuildSettings.scalaSettings: _*)
  .settings(DefaultBuildSettings.defaultSettings())
  .settings(SbtDistributablesPlugin.publishingSettings: _*)
  .settings(playSettings: _*)
  .settings(
    majorVersion := 0,
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation"
    ),
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test
  )
  .configs(IntegrationTest)
  .settings(integrationTestSettings(): _*)
  .settings(resolvers ++= Seq(
    Resolver.bintrayRepo("hmrc", "releases"),
    Resolver.jcenterRepo
  ))

lazy val playSettings = Seq(
  //  PlayKeys.playDefaultPort  := 8067,
  RoutesKeys.routesImport += "models._",
  TwirlKeys.templateImports ++= Seq(
    "play.twirl.api.HtmlFormat",
    "play.twirl.api.HtmlFormat._"
  ),
  Concat.groups := Seq(
    "javascripts/application.js" -> group(Seq("lib/govuk-frontend/all.js"))
  ),
  uglifyCompressOptions := Seq(
    "unused=false",
    "dead_code=false"
  ),
  pipelineStages := Seq(digest),
  pipelineStages in Assets := Seq(concat, uglify)
)
