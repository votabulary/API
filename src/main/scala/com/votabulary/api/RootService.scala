package com.votabulary.api

import akka.actor.Actor
import spray.routing._
import directives.LogEntry
import spray.http._
import MediaTypes._
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
  def receive = runRoute(rootRoute)
}

// this trait defines our service behavior independently from the service actor
trait RootService extends HttpService with StaticResources with MemberService {

  def showPath(req: HttpRequest) = req.method match {
    case HttpMethods.POST => LogEntry("Method = %s, Path = %s, Data = %s" format(req.method, req.uri, req.entity), Logging.InfoLevel)
    case _ => LogEntry("Method = %s, Path = %s" format(req.method, req.uri), Logging.InfoLevel)
  }

  val rootRoute =
    logRequest(showPath _) {
      memberRoute ~ staticResources
    }
}
/*
// TODO implement your REST Api
trait Api extends HttpService {

  val memberRoute =
    get {
      path("member") {
        respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <h1>Votabulary member registration!</h1>
              </body>
            </html>
          }
        }
      }
    }

}
*/
// Trait for serving static resources
// Sends 404 for 'favicon.icon' requests and serves static resources in 'bootstrap' folder.
trait StaticResources extends HttpService {

  val staticResources =
    get {
      path("") {
        redirect("/index.html")
      } ~
        path("favicon.ico") {
          complete(StatusCodes.NotFound)
        } ~
        path(Rest) { path =>
          getFromResource("root/%s" format path)
        }
    }
}