package com.votabulary.model

import com.typesafe.config.ConfigFactory
import com.votabulary.model.member.MemberComponent
import spray.routing.directives.LogEntry
import akka.event.Logging

import scala.slick.driver.{MySQLDriver, ExtendedProfile}
import scala.slick.lifted.DDL
import scala.slick.session.Session

/**
 * Created by jason on 6/29/14.
 */
trait Profile {
  val profile: ExtendedProfile
}

class DAL(override val profile: ExtendedProfile) extends MemberComponent with Profile {
  import profile.simple._

  def ddls = Set(Members.ddl)

  def create(ddl: DDL)(implicit s: Session): Unit = try {
    ddl create
  } catch {
    case ex: Exception => LogEntry("Could not create table.... assuming it already exists", Logging.DebugLevel)
  }

  def create(implicit s: Session): Unit = ddls.foreach(create(_))

  def drop(ddl: DDL)(implicit s: Session): Unit = try {
    ddl drop
  } catch {
    case ex: Exception => LogEntry("Could not drop table", Logging.DebugLevel)
  }

  def drop(implicit s: Session): Unit = ddls.foreach(drop(_))

  def purge(implicit s: Session) = { drop; create }

}

object DAL {

  val config = ConfigFactory.load()
  config.checkValid(ConfigFactory.defaultReference())

  def apply(): DAL = new DAL(MySQLDriver)

  // Clients can import this rather than depending on slick directly
  implicit def threadLocalSession: Session = scala.slick.session.Database.threadLocalSession

}