package com.votabulary.model

import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.{Logger, LoggerFactory}
import scala.slick.session.Database
import com.mchange.v2.c3p0.ComboPooledDataSource
import scala.util.Properties

trait DBConn {
  val database: Database
}

object DBConn extends DBConn {
  lazy val database: Database = DatabaseConnection.database
}

object DatabaseConnection {

  val log: Logger = LoggerFactory.getLogger("DatabaseConnection")

  def database: Database = {

    try {
      val pool = new ComboPooledDataSource
      //load the URL/Driver/User/PW from config
      val config: Config = ConfigFactory.load()
      config.checkValid(ConfigFactory.defaultReference())

      val env = config.getString("build.env")
      val jdbc = JDBCURL(config)
      pool.setJdbcUrl(jdbc.url)
      pool.setUser(jdbc.user)
      pool.setPassword(jdbc.password)
      pool.setDriverClass(config.getString("environment." + env + ".db.driver"))
      pool.setMinPoolSize(config.getInt("environment." + env + ".cp.minPoolSize"))
      pool.setAcquireIncrement(config.getInt("environment." + env + ".cp.acquireIncrement"))
      pool.setMaxPoolSize(config.getInt("environment." + env + ".cp.maxPoolSize"))
      pool.setMaxIdleTime(config.getInt("environment." + env + ".cp.maxIdleTime"))
      pool.setTestConnectionOnCheckout(config.getBoolean("environment." + env + ".cp.testConnectionOnCheckout"))
      pool.setPreferredTestQuery(config.getString("environment." + env + ".cp.preferredTestQuery"))
      pool.getProperties.put("utf8", "true")

      println(s"JDBC URL: ${pool.getJdbcUrl}")

      Database forDataSource pool

    } catch {
      case e: InstantiationException => {
        log.error("Unable to get DB connection.", e)
        throw new RuntimeException(e)
      }
      case unknown: Throwable => {
        log.error("unhandled exception while getting DB connection.", unknown)
        throw new RuntimeException(unknown)
      }
    }
  }
}

case class JDBCURL(url: String, user: String, password: String) {}

object JDBCURL {

  val ENV_VAR: String = "CLEARDB_DATABASE_URL"

  def apply(config: Config): JDBCURL = {
    Properties.envOrNone(ENV_VAR) match {
      case Some(value) => // Assume: jdbc:mysql://user:password@url
        println(s"Read URL from env var: $value")
        val credsUrl = value.split("//")(1).split("@")
        val creds = credsUrl(0).split(":")
        val url = s"jdbc:mysql://${credsUrl(1)}"
        new JDBCURL(url, creds(0), creds(1))
      case None =>
        val env = config.getString("build.env")
        val url = config.getString("environment." + env + ".db.url")
        val user = config.getString("environment." + env + ".db.user")
        val password = config.getString("environment." + env + ".db.password")
        new JDBCURL(url, user, password)
    }
  }

}