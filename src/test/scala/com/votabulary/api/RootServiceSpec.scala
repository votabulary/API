package com.votabulary.api

import org.specs2.mutable.Specification
import spray.http.StatusCodes._
import spray.http._
import spray.testkit.Specs2RouteTest


class RootServiceSpec extends Specification with Specs2RouteTest with RootService {
  def actorRefFactory = system
  
  "RootService" should {

    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> rootRoute ~> check {
        handled must beFalse
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put() ~> sealRoute(rootRoute) ~> check {
        status === MethodNotAllowed
        entityAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }
  }
}