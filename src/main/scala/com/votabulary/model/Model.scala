package com.votabulary.model

import scala.slick.session.Database

class Model(name: String, val dal: DAL, val db: Database) {
  // We only need the DB/session imports outside the DAL

  // Put an implicitSession in scope for database actions
  implicit val session = db.createSession

  def createDB = dal.create

  def dropDB = dal.drop

  def purgeDB = dal.purge

}