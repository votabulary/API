package com.votabulary.model

import com.votabulary.model.member.MemberDAL_OLD

import scala.slick.session.Database

class Model(name: String, val dal: DAL, val db: Database) extends MemberDAL_OLD {
  // We only need the DB/session imports outside the DAL

  // Put an implicitSession in scope for database actions
  implicit val session = db.createSession

  def createDB = dal.create

  def dropDB = dal.drop

  def purgeDB = dal.purge

}