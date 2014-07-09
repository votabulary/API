package com.votabulary.model

import java.util.Properties

import scala.slick.driver.H2Driver
import scala.slick.driver.MySQLDriver
import scala.slick.session.{Database, Session}
import com.typesafe.config.ConfigFactory
import javax.sql.DataSource
import com.mchange.v2.c3p0.ComboPooledDataSource
import scala.slick.driver.H2Driver.simple._

trait DBConfig {

  val config = ConfigFactory.load()
  config.checkValid(ConfigFactory.defaultReference())

  val env = config.getString("build.env")

  def m = env match {
    case "vt.test" =>
      val props = new Properties()
      props.setProperty("MODE", "MySQL")
      new Model("H2TestDB", new DAL(H2Driver, DBConn), Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver", prop = props))
    case _ =>
      val user = config.getString(s"environment.$env.db.user")
      val password = config.getString(s"environment.$env.db.password")
      val dbURL = config.getString(s"environment.$env.db.url")

      val dataSource: DataSource = {
        val ds = new ComboPooledDataSource
        ds.setDriverClass("com.mysql.jdbc.Driver")
        ds.setUser(config.getString(s"environment.$env.db.user"))
        ds.setPassword(config.getString(s"environment.$env.db.password"))
        ds.setMaxPoolSize(config.getInt(s"environment.$env.cp.maxPoolSize"))
        ds.setMaxIdleTime(config.getInt(s"environment.$env.cp.maxIdleTime"))
        ds.setTestConnectionOnCheckout(config.getBoolean(s"environment.$env.cp.testConnectionOnCheckout"))
        ds.setAcquireIncrement(config.getInt(s"environment.$env.cp.acquireIncrement"))
        ds.getProperties.put("utf8", "true")
        ds.setIdleConnectionTestPeriod(60)
        ds.setPreferredTestQuery(config.getString(s"environment.$env.cp.preferredTestQuery"))
        ds.setJdbcUrl(config.getString(s"environment.$env.db.url"))
        ds
      }

      // test the data source validity
      dataSource.getConnection().close()

      new Model("ClearDB", new DAL(MySQLDriver, DBConn), Database.forDataSource(dataSource))
  }

//  m.createDB
  m.purgeDB

}
