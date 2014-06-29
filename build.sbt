import com.typesafe.sbt.SbtStartScript

organization  := "com.votabulary"

version       := "0.1.0"

scalaVersion  := "2.10.0"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

unmanagedResourceDirectories in Compile <++= baseDirectory { base =>
    Seq( base / "src/main/webapp" )
}

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/"
)

libraryDependencies ++= {
    val sprayVersion = "1.1-M7"
    val akkaVersion  = "2.1.0"
    Seq(
      "io.spray"            %   "spray-can"     % sprayVersion,
      "io.spray"            %   "spray-routing" % sprayVersion,
      "io.spray"            %   "spray-testkit" % sprayVersion,
      "io.spray"            %%  "spray-json"    % "1.2.5",
      "com.typesafe.akka"   %%  "akka-actor"    % akkaVersion,
      "org.specs2"          %%  "specs2"        % "1.13" % "test",
      "org.scalatest"       %   "scalatest_2.10" % "2.0.M5b" % "test",
      "org.seleniumhq.selenium" % "selenium-java" % "2.28.0" % "test"
    )
}

seq(Revolver.settings: _*)

seq(SbtStartScript.startScriptForClassesSettings: _*)
