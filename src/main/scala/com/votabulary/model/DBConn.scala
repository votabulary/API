package com.votabulary.model

import scala.slick.session.Database
import com.mchange.v2.c3p0.ComboPooledDataSource
import org.slf4j.LoggerFactory
import util.Properties
import java.net.URI

trait DBConn {
  
  val database: Database

}

object DatabaseConnection {

  val log = LoggerFactory.getLogger("DatabaseConnection")

  def database: Database = {
    import com.typesafe.config._

    try {
      val pool = new ComboPooledDataSource
      //load the URL/Driver/User/PW from config
      val config = ConfigFactory.load()
      config.checkValid(ConfigFactory.defaultReference())

      val env = config.getString("build.env")
      val url = config.getString("environment." + env + ".db.url")
      val user = config.getString("environment." + env + ".db.user")
      val password = config.getString("environment." + env + ".db.password")
      val fullURL = new URI(Properties.envOrElse("CLEARDB_DATABASE_URL", s"$user:$password@$url"))
      val dbUrl = s"jdbc:mysql://${fullURL.getHost()}${fullURL.getPath()}"
      val driver = config.getString("environment." + env + ".db.driver")
      log.debug("env: " + env)
      log.debug("URL: " + dbUrl)
      log.debug("Driver: " + driver)
      log.debug("User: " + user)
      log.debug("Password: " + password)
      pool.setDriverClass(driver)
      pool.setJdbcUrl(dbUrl)
      pool.setUser(user)
      pool.setPassword(password)
      val minSize = config.getInt("environment." + env + ".cp.minPoolSize")
      val maxSize = config.getInt("environment." + env + ".cp.maxPoolSize")
      val acquireIncrement = config.getInt("environment." + env + ".cp.acquireIncrement")
      val maxIdleTime = config.getInt("environment." + env + ".cp.maxIdleTime")
      val testConnectionOnCheckout = config.getBoolean("environment." + env + ".cp.testConnectionOnCheckout")
      val preferredTestQuery = config.getString("environment." + env + ".cp.preferredTestQuery")
      pool.setMinPoolSize(minSize)
      pool.setAcquireIncrement(acquireIncrement)
      pool.setMaxPoolSize(maxSize)
      pool.setMaxIdleTime(maxIdleTime)
      pool.setTestConnectionOnCheckout(testConnectionOnCheckout)
      pool.setPreferredTestQuery(preferredTestQuery)
      pool.getProperties.put("utf8", "true")

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

object DBConn extends DBConn {
  lazy val database: Database = DatabaseConnection.database
}