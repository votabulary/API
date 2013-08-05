package com.example

import spray.can.server.SprayCanHttpServerApp
import akka.actor.Props

trait WebApp extends SprayCanHttpServerApp {

  // create and start our service actor
  val service = system.actorOf(Props[MyServiceActor], "my-service")

  // To run project on Heroku, get PORT from environment
  val httpHost = "0.0.0.0"
  val httpPort = Option(System.getenv("PORT")).getOrElse("8080").toInt

  // create a new HttpServer using our handler tell it where to bind to
  newHttpServer(service) ! Bind(interface = httpHost, port = httpPort)
}

object Boot extends App with WebApp {

}