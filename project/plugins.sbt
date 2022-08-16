// The Typesafe repository
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.16")

// play swagger plugin - https://github.com/iheartradio/play-swagger
addSbtPlugin("com.iheart" % "sbt-play-swagger" % "0.10.8")

// scalafmt plugin for scala formatter - https://github.com/scalameta/sbt-scalafmt
// sbt scalafmtCheckAll
// sbt scalafmtAll
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.6")

// https://github.com/Philippus/sbt-dotenv
addSbtPlugin("nl.gn0s1s" % "sbt-dotenv" % "3.0.0")

// https://www.baeldung.com/scala/sbt-dependency-tree
// sbt dependencyBrowseGraph
// sbt dependencyBrowseTree
addDependencyTreePlugin

// https://github.com/rtimush/sbt-updates
// sbt dependencyUpdates
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.6.3")

// https://github.com/DavidGregory084/sbt-tpolecat
// addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.20")

// https://github.com/scoverage/sbt-scoverage
// https://github.com/scoverage/scalac-scoverage-plugin
// sbt sbtScoverageDoc
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.9.3")

// https://github.com/albuch/sbt-dependency-check
addSbtPlugin("net.vonbuchholtz" % "sbt-dependency-check" % "4.1.0")
