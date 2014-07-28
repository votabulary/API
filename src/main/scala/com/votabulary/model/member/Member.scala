package com.votabulary.model.member

import com.votabulary.model.Profile
import com.votabulary.model.DAL

import scala.slick.session.Session

/**
 * Created by jason on 6/29/14.
 */
case class Member(id: Option[Long] = None,
                  firstName: String,
                  lastName: String,
                  email: String,
                  state: String = "Texas",
                  county: String = "Travis",
                  precinct: Int,  // Change to String?
                  emailReminder: Boolean,
                  smsReminder: Boolean,
                  smsNumber: String) {

  require("""(\w+)@([\w\.]+)""".r.unapplySeq(email).isDefined, "Provided email is not valid.")
  require(!firstName.isEmpty, "First name is required")
  require(!lastName.isEmpty, "Last name is required")
  require(!state.isEmpty, "State is required")
  require(!county.isEmpty, "County is required")
  require(precinct >= 101 && precinct <= 468, "Precinct is invalid. Please visit: http://www.traviscountytax.org/gis/maps/0.htm")

}

object MemberDAL {

  private val dal = DAL.apply

  import dal.Members

  def all: Set[Member] = dal.db.withSession { implicit s: Session =>
    Members.all
  }

  def get(id: Long) = dal.db.withSession { implicit s: Session =>
    Members.get(id)
  }

  def get(email: String) = dal.db.withSession { implicit s: Session =>
    Members.get(email)
  }

  def insert(member: Member) = dal.db.withSession { implicit s: Session =>
    Members.insert(member)
  }

  def update(member: Member) = dal.db.withSession { implicit s: Session =>
    Members.update(member)
  }

}

trait MemberComponent { this: Profile =>
  import profile.simple._

  object Members extends Table[Member]("member") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc, O.NotNull)
    def first = column[String]("first_name", O.NotNull)
    def last = column[String]("last_name", O.NotNull)
    def email = column[String]("email", O.NotNull)
    def state = column[String]("state", O.NotNull)
    def county = column[String]("country", O.NotNull)
    def precinct = column[Int]("precinct", O.NotNull)
    def emailReminder = column[Boolean]("email_reminder", O.NotNull)
    def smsReminder = column[Boolean]("sms_reminder", O.NotNull)
    def smsNumber = column[String]("sms_number", O.Nullable)

    def proj = id.? ~ first ~ last ~ email ~ state ~ county ~ precinct ~ emailReminder ~ smsReminder ~ smsNumber
    def * = proj <> (Member, Member.unapply _)

    def all(implicit session: Session): Set[Member] =
      (for (m <- Members) yield m).list.toSet

    def get(id: Long)(implicit session: Session): Option[Member] =
      (for (m <- Members if m.id === id) yield m).firstOption

    def get(email: String)(implicit session: Session): Option[Member] =
      (for (m <- Members if m.email === email) yield m).firstOption

    def insert(m: Member)(implicit session: Session): Member = {
      // TODO Is this the best way to impliment this?
      val id = proj.insert(None, m.firstName, m.lastName, m.email, m.state, m.county, m.precinct, m.emailReminder, m.smsReminder, m.smsNumber)
      get(id).get
    }

    def update(m: Member)(implicit s: Session): Member = {
      val q = for (m <- Members if m.id === m.id) yield m
      q.update(m)
      get(m.id.get).get
    }
  }
}
