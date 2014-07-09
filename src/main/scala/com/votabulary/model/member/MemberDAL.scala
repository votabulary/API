package com.votabulary.model.member

import com.votabulary.model.DAL

import scala.slick.session.{Session, Database}

/**
 * Created by jason on 6/29/14.
 */
trait MemberDAL_OLD {

  val dal: DAL
  val db: Database

  import dal._

  implicit val session: Session

  def allMembers = Members.all

  def getMember(id: Long) = Members.get(id)

  def getMember(email: String) = Members.get(email)

  def addMember(member: Member)= Members.insert(member)

  def updateMember(member: Member) = Members.update(member)

}
