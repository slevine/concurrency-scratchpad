name := "concurrency-scratchpad"

version := "1.0"

libraryDependencies ++= Seq(
  "com.typesafe" %% "scalalogging-slf4j" % "1.0.1",
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "org.scala-lang.modules" %% "scala-async" % "0.9.1",
  "com.typesafe.akka" %% "akka-actor" % "2.3.2"
)


