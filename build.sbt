lazy val server = (project in file("server")).settings(commonSettings).settings(
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  pipelineStages := Seq(digest, gzip),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
  libraryDependencies ++= Seq(
    "com.vmunier" %% "scalajs-scripts" % "1.1.1",
    "com.typesafe.play" %% "play-slick" % "3.0.0",
    "com.typesafe.play" %% "play-slick-evolutions" % "3.0.0",
    "com.h2database" % "h2" % "1.4.196",
    "com.dripower" %% "play-circe" % "2609.0",
    "io.swagger" %% "swagger-play2" % "1.6.0",
    "org.webjars" % "swagger-ui" % "3.10.0",
    guice,
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test"
  ),
  swaggerDomainNameSpaces := Seq("models"),
  // Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
  EclipseKeys.preTasks := Seq(compile in Compile)
).enablePlugins(PlayScala, SwaggerPlugin)
  .disablePlugins(PlayFilters)
  .dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(commonSettings).settings(
  scalaJSUseMainModuleInitializer := true,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.5",
    "com.lihaoyi" %%% "scalatags" % "0.6.7",
    "org.querki" %%% "jquery-facade" % "1.2",
    "io.circe" %%% "circe-core" % "0.9.3",
    "io.circe" %%% "circe-generic" % "0.9.3",
    "io.circe" %%% "circe-parser" % "0.9.3"
  ),
  jsDependencies ++= Seq(
    "org.webjars" % "jquery" % "2.2.1" / "jquery.js"
      minified "jquery.min.js",
    "org.webjars" % "notifyjs" % "0.4.2" / "notify.js")
).enablePlugins(ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).settings(commonSettings)
lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val commonSettings = Seq(
  scalaVersion := "2.12.4",
  organization := "io.fscala"
)

// loads the server project at sbt startup
onLoad in Global := (onLoad in Global).value andThen {s: State => "project server" :: s}
