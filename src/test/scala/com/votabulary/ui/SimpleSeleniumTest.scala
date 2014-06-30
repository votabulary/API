package com.votabulary.ui

import com.votabulary.WebApp
import org.scalatest._
import org.scalatest.selenium.HtmlUnit

class SimpleSeleniumTest extends FlatSpec with WebApp with Matchers with BeforeAndAfterAll with HtmlUnit {

  behavior of "Spray Web Application"

  it should "display a home page containing 3 links" in {
    go to ("http://localhost:8080")
    val h1: Option[SimpleSeleniumTest.this.type#Element] = find(xpath("//h1"))

    h1 match {
      case Some(elem) => elem.text should be("Lorem ipsum")
      case None => fail("h1 not found")
    }
  }

  override protected def afterAll() {
    quit
    system.shutdown
  }
}
