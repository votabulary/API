package com.votabulary

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import com.typesafe.config.ConfigFactory
import com.votabulary.api.RootServiceActor
import com.votabulary.model.DAL
import spray.can.Http
//import spray.can.server.SprayCanHttpServerApp
/*
trait WebApp extends SprayCanHttpServerApp {

  val config = ConfigFactory.load()

  val env = config.getString("build.env")
  println(s"Running with build environment: $env")

  val dal = DAL()

  // create and start our service actor
  val service = system.actorOf(Props[RootServiceActor], "votabulary-service")

  // To run project on Heroku, get PORT from environment
  val httpHost = "0.0.0.0"
  val httpPort = Option(System.getenv("PORT")).getOrElse("8080").toInt

  // create a new HttpServer using our handler tell it where to bind to
  newHttpServer(service) ! Bind(interface = httpHost, port = httpPort)
}
*/
object Boot extends App {//with WebApp {

  implicit val system = ActorSystem()

  val config = ConfigFactory.load()

  val env = config.getString("build.env")
  println(s"Running with build environment: $env")

  // create and start our service actor
  val service = system.actorOf(Props[RootServiceActor], "votabulary-service")

  // To run project on Heroku, get PORT from environment
  val httpHost = "0.0.0.0"
  val httpPort = Option(System.getenv("PORT")).getOrElse("8080").toInt

  // create a new HttpServer using our handler tell it where to bind to
  IO(Http) ! Http.Bind(service, httpHost, httpPort)
//  IO(Http) ! Bind(interface = httpHost, port = httpPort)

}