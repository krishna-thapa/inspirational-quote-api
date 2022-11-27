import play.sbt.PlayImport.guice
import sbt._

object Dependencies {

  object Versions {
    val playSlick = "5.0.2"
    val slickPg = "0.20.3"
    val swagger = "3.43.0"
    val playjwt = "5.0.0"
    val bcrypt = "4.3.0"
    val quartzScheduler = "1.9.3-akka-2.6.x"
    val akkaVersion = "2.6.19"

    // Database
    val elastic4s = "7.9.1"
    val playRedis = "2.7.0"
    val postgresql = "42.4.3"

    // tests
    val scalaPlayTest = "5.1.0"
    val testContainers = "0.40.10"
    val mockitoVer = "1.17.12"

    // logs
    val logbackEncoder = "7.2"
  }

  object Libraries {
    val swaggerUi = "org.webjars" % "swagger-ui" % Versions.swagger
    val playJwt = "com.pauldijou" %% "jwt-play" % Versions.playjwt
    val scalaBcrypt = "com.github.t3hnar" %% "scala-bcrypt" % Versions.bcrypt
    val quartzScheduler = "com.enragedginger" %% "akka-quartz-scheduler" % Versions.quartzScheduler

    // Akka
    lazy val akka = Seq(
      "com.typesafe.akka" %% "akka-stream" % Versions.akkaVersion,
      "com.typesafe.akka" %% "akka-actor-typed" % Versions.akkaVersion,
      "com.typesafe.akka" %% "akka-serialization-jackson" % Versions.akkaVersion,
      "com.typesafe.akka" %% "akka-slf4j" % Versions.akkaVersion,
      "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "3.0.4",
      "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "3.0.4"
    )

    // Slick for the Postgres DB
    lazy val slickDatabaseDependencies = Seq(
      "org.postgresql" % "postgresql" % Versions.postgresql,
      "com.typesafe.play" %% "play-slick" % Versions.playSlick,
      "com.typesafe.play" %% "play-slick-evolutions" % Versions.playSlick,
      "com.github.tminglei" %% "slick-pg" % Versions.slickPg,
      "com.github.tminglei" %% "slick-pg_play-json" % Versions.slickPg
    )

    // Elastic Search dependencies
    lazy val elastic4sDependencies = Seq(
      "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % Versions.elastic4s,
      "com.sksamuel.elastic4s" %% "elastic4s-json-play" % Versions.elastic4s,
      "com.sksamuel.elastic4s" %% "elastic4s-http-streams" % Versions.elastic4s
    )

    val playRedis = "com.github.karelcemus" %% "play-redis" % Versions.playRedis

    val scalaTestPlus =
      "org.scalatestplus.play" %% "scalatestplus-play" % Versions.scalaPlayTest % "test"

    val testContainer =
      "com.dimafeng" %% "testcontainers-scala-scalatest" % Versions.testContainers % "test"

    val mockitoSugar = "org.mockito" %% "mockito-scala-scalatest" % Versions.mockitoVer % "test"

    // Docker test container
    lazy val testContainerDependencies = Seq(
      testContainer,
      scalaTestPlus,
      mockitoSugar,
      "com.dimafeng" %% "testcontainers-scala-postgresql" % Versions.testContainers % "test"
    )

    // Docker test container test kits with Mockito
    lazy val dockerTestKitWithMock = Seq(
      testContainer,
      mockitoSugar,
      "com.dimafeng" %% "testcontainers-scala-elasticsearch" % Versions.testContainers % "test"
    )

    lazy val commonDependencies = Seq(
      guice,
      scalaTestPlus,
      "net.logstash.logback" % "logstash-logback-encoder" % Versions.logbackEncoder excludeAll ExclusionRule(
        organization = "com.fasterxml.jackson.core"
      )
    )

  }

}
