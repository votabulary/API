package com.votabulary.model.member

import org.scalatest.{FlatSpec, BeforeAndAfter}
import org.scalatest._

/**
 * Created by jason on 6/29/14.
 */
class MemberSpec extends FlatSpec with Matchers with BeforeAndAfter {

  before {
    // Create new empty schema
  }

  after {
    // Drop schema
  }

  "A member" should "have a default state value of Texas" in {
    val m = Member(firstName = "Jason", lastName = "Schmitt", email = "jason@votabulary.com", precinct = 242, emailReminder = true, smsReminder = true, smsNumber = "5127626443")
    m.state should be ("Texas")
  }

  it should "have a default county value of Travis" in {
    val m = Member(firstName = "Jason", lastName = "Schmitt", email = "jason@votabulary.com", precinct = 242, emailReminder = true, smsReminder = true, smsNumber = "5127626443")
    m.county should be ("Travis")
  }

  it should "throw IllegalArgumentException for invalid email address" in {
    an [IllegalArgumentException] should be thrownBy {
      Member(None, "Jason", "Schmitt", "invalid email", "Texas", "Travis", 242, true, true, "5127626443")
    }
  }

  it should "throw IllegalArgumentException for empty first name" in {
    an [IllegalArgumentException] should be thrownBy {
      Member(None, "", "Schmitt", "jason@votabulary.com", "Texas", "Travis", 242, true, true, "5127626443")
    }
  }

  it should "throw IllegalArgumentException for empty last name" in {
    an [IllegalArgumentException] should be thrownBy {
      Member(None, "Jason", "", "jason@votabulary.com", "Texas", "Travis", 242, true, true, "5127626443")
    }
  }

  it should "throw IllegalArgumentException for empty state" in {
    an [IllegalArgumentException] should be thrownBy {
      Member(None, "Jason", "Schmitt", "jason@votabulary.com", "", "Travis", 242, true, true, "5127626443")
    }
  }

  it should "throw IllegalArgumentException for empty county" in {
    an [IllegalArgumentException] should be thrownBy {
      Member(None, "Jason", "Schmitt", "jason@votabulary.com", "Texas", "", 242, true, true, "5127626443")
    }
  }

  it should "throw IllegalArgumentException for invalid precinct number" in {
    an [IllegalArgumentException] should be thrownBy {
      Member(None, "", "Schmitt", "jason@votabulary.com", "Texas", "Travis", 1, true, true, "5127626443")
    }
  }

}
