package com.votabulary.api

import akka.actor.Actor
import com.votabulary.model.{DAL, DBConfig}
import spray.routing._
import directives.LogEntry
import spray.http._
import akka.event.Logging

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class RootServiceActor extends Actor with RootService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(rootRoute ~ memberRoute)
}

// this trait defines our service behavior independently from the service actor
trait RootService extends HttpService with MemberService {

  def showPath(req: HttpRequest) = req.method match {
    case HttpMethods.POST => LogEntry("Method = %s, Path = %s, Data = %s" format(req.method, req.uri, req.entity), Logging.InfoLevel)
    case _ => LogEntry("Method = %s, Path = %s" format(req.method, req.uri), Logging.InfoLevel)
  }

  val rootRoute =
    get {
      path("") {
//        redirect("http://www.votabulary.com")
        complete(StatusCodes.NotFound)
      } ~
        path("favicon.ico") {
          complete(StatusCodes.NotFound)
        } ~
        path(Rest) { path =>
          getFromResource("root/%s" format path)
        }
    }

  /*
  val rootRoute =
    logRequest(showPath _) {
      memberRoute ~ staticResources
    }
  */
}
/*
// Trait for serving static resources
// Sends 404 for 'favicon.icon' requests and serves static resources in 'bootstrap' folder.
trait StaticResources extends HttpService { this: DBConfig =>

  val staticResources =
    get {
      path("") {
        redirect("http://www.votabulary.com")
      } ~
        path("favicon.ico") {
          complete(StatusCodes.NotFound)
        } ~
        path(Rest) { path =>
          getFromResource("root/%s" format path)
        }
    }
}
*/