package com.votabulary.api

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import spray.http.StatusCodes._
import spray.http._
import spray.testkit.ScalatestRouteTest


class RootServiceTest extends FlatSpec with ScalatestRouteTest with ShouldMatchers with RootService {
  def actorRefFactory = system

  behavior of "MyService"

  it should "leave GET requests to other paths unhandled" in {
    Get("/kermit") ~> rootRoute ~> check {
      handled should be(false)
    }
  }

  it should "return a MethodNotAllowed error for PUT requests to the root path" in {
    Put() ~> sealRoute(rootRoute) ~> check {
      status should be(MethodNotAllowed)
      entityAs[String] should be("HTTP method not allowed, supported methods: GET")
    }
  }
}