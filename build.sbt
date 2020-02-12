import sbtassembly.AssemblyKeys.assemblyMergeStrategy
import sbtassembly.MergeStrategy

name := "MorphologicalAnalyzer"

version := "0.1"

scalaVersion := "2.12.7"

val colossus = "com.tumblr" %% "colossus" % "0.11.0"
val spray = "io.spray" %%  "spray-json" % "1.3.3"
val bridj =  "com.nativelibs4java" % "bridj" % "0.6.2"
val kuromoji = "com.atilika.kuromoji" % "kuromoji-core" % "0.9.0"
val kuromojiIpadic =  "com.atilika.kuromoji" % "kuromoji-ipadic" % "0.9.0"
//val stancntag = "com.guokr" % "stan-cn-ner" % "0.0.6"
val stanfordnlp = "edu.stanford.nlp" % "stanford-corenlp" % "3.9.2"


lazy val commonSettings = Seq(
  version := "0.1-SNAPSHOT",
  organization := "morphological.analyzer",
  scalaVersion := "2.12.7",
  test in assembly := {},
  fork in Test := true,
  libraryDependencies ++= Seq(colossus,spray,bridj,kuromoji,kuromojiIpadic,stanfordnlp),
  resolvers ++= Seq(
    Resolver.mavenLocal,
    "Sbt plugins"  at "https://dl.bintray.com/sbt/sbt-plugin-releases",
    "Concurrent Maven Repo" at "https://conjars.org/repo", // for cascading
    "Central Maven Repo" at "https://central.maven.org/maven2/"
  )
)

lazy val morphological = (project in file("morphological")).
  settings(
    name := "morphorogical",
    assemblyMergeStrategy in assembly := {
      case PathList("edu", "stanford" , "nlp", xs @ _*) => MergeStrategy.first
      case x if x.endsWith("module-info.class") => MergeStrategy.first
      case x if x.endsWith("Among.class") => MergeStrategy.last
      case x if x.endsWith("SnowballProgram.class") => MergeStrategy.last
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  ).
  settings(commonSettings: _*)
//settings(libraryDependencies ++= componentDependencies).
