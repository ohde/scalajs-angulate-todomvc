name := "scalajs-angulate-todomvc"

val commonSettings = Seq(
  organization := "biz.enef",
  version := "0.1",
  scalaVersion := "2.11.6",
  scalacOptions ++= Seq("-deprecation","-feature","-Xlint"),
  resolvers += "karchedon-repo" at "http://maven.karchedon.de/",
  resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

val angulateDebugFlags = Seq(
  "runtimeLogging",
  "HttpPromiseMacros.debug"
).map( f => s"-Xmacro-settings:biz.enef.angular.$f" )
  
lazy val js = project.
  settings(commonSettings: _*).
  settings(
    scalacOptions ++= angulateDebugFlags,
    persistLauncher in Compile := true,
    persistLauncher in Test := false,
    libraryDependencies ++= Seq(
      "biz.enef" %%% "scalajs-angulate" % "0.1"
    )
  ).
  enablePlugins(ScalaJSPlugin)

lazy val app = project.in( file(".") ).
  settings(commonSettings:_*).
  settings(
    // build JS and add JS resources
    (compile in Compile) <<= (compile in Compile).dependsOn(fastOptJS in (js,Compile)),
    (compile in Compile) <<= (compile in Compile).dependsOn(fullOptJS in (js,Compile)),
    mainClass in (Compile,run) := None
  )
    
