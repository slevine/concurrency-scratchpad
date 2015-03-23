name := "concurrency-scratchpad"

version := "1.0"

autoCompilerPlugins := true

scalacOptions += "-P:continuations:enable"

libraryDependencies <+= scalaVersion {
  v => compilerPlugin("org.scala-lang.plugins" % "continuations" % "2.10.2")
}

libraryDependencies ++= Seq(
  "com.typesafe" %% "scalalogging-slf4j" % "1.0.1",
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "org.scala-lang.modules" %% "scala-async" % "0.9.1",
  "com.typesafe.akka" %% "akka-actor" % "2.3.2",
  "com.typesafe.akka" %% "akka-dataflow" % "2.3-M1"
)


