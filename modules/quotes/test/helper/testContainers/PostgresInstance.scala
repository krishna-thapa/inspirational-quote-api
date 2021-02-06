package helper.testContainers

import com.dimafeng.testcontainers.PostgreSQLContainer
import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import helper.{ GenericFlatSpec, LoadQueries }
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import slick.jdbc.JdbcBackend
import slick.jdbc.JdbcBackend.Database

trait PostgresInstance
    extends GenericFlatSpec
    with GuiceOneAppPerSuite
    with TestContainerForAll
    with LoadQueries {

  // Define a Postgres Container for test docker
  override val containerDef: PostgreSQLContainer.Def = PostgreSQLContainer.Def()

  // Start the connection
  val container: PostgreSQLContainer = startContainers()

  // Initialize the Database connection using PostgreSQLContainer
  override val dbConfig: JdbcBackend.DatabaseDef = {
    Database.forURL(
      url = container.jdbcUrl,
      user = container.username,
      password = container.password,
      driver = "org.postgresql.Driver"
    )
  }

  // Create and inject the instance of DatabaseConfigProvider
  implicit override lazy val app: Application = new GuiceApplicationBuilder()
    .configure(
      "slick.dbs.default.profile"          -> "slick.jdbc.PostgresProfile$",
      "slick.dbs.default.db.driver"        -> "org.postgresql.Driver",
      "slick.dbs.default.db.url"           -> container.jdbcUrl,
      "slick.dbs.default.db.user"          -> container.username,
      "slick.dbs.default.db.password"      -> container.password,
      "play.evolutions.db.default.enabled" -> "false" // Important to disable evolution while running test
    )
    .build()

}