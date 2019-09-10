import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  val compile = Seq(

    "uk.gov.hmrc" %% "play-health"                   % "3.9.0-play-26",
    "uk.gov.hmrc" %% "bootstrap-play-26"             % "0.33.0",
    "uk.gov.hmrc" %% "play-ui"                       % "7.27.0-play-26",
    "uk.gov.hmrc" %% "play-frontend-govuk"           % "0.1.0-play-26-SNAPSHOT"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-play-26"        % "0.46.0" % Test classifier "tests",
    "org.scalatest"           %% "scalatest"                % "3.0.8"                 % "test",
    "org.jsoup"               %  "jsoup"                    % "1.10.2"                % "test",
    "com.typesafe.play"       %% "play-test"                % current                 % "test",
    "org.pegdown"             %  "pegdown"                  % "1.6.0"                 % "test, it",
    "org.scalatestplus.play"  %% "scalatestplus-play"       % "3.1.2"                 % "test, it"
  )

}
