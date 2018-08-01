name := "proyecto"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-cache" % "2.6.12",
  "com.typesafe.akka" %% "akka-http" % "10.0.11",
  "com.typesafe.akka" %% "akka-actor" % "2.5.11",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.0",
  "com.typesafe.akka" %% "akka-http-caching" % "10.1.0",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "com.typesafe.play" %% "play-json" % "2.6.9",
  "de.heikoseeberger" %% "akka-http-play-json" % "1.20.0",
  //"com.typesafe.slick" %% "slick" % "3.2.3",
  "com.typesafe.play" %% "play-slick" % "3.0.3",
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc4",
  "org.slf4j" % "slf4j-nop" % "1.6.4" ,
  //"com.zaxxer" % "HikariCP" % "2.4.1"
"com.typesafe.slick" %% "slick-hikaricp" % "3.2.0"

)