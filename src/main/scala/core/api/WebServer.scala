package core.api


import akka.actor.{ActorRef, ActorSelection}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{HttpApp, Route}
import akka.pattern.Patterns
import core._

import scala.collection.mutable
import scala.concurrent.{Future}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

case class MicroService(name: String, state: Boolean)

case object MicroService extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val MicroServiceFormat = jsonFormat2(MicroService.apply)
}


class WebServer extends HttpApp{

  var orchestrator: ActorSelection = null


  override def routes(): Route = cors(){

      path("actor" / Remaining) { pathRest =>
        get {
            val actorRef = Patterns.ask(orchestrator, FindByName(pathRest), 50000)
            val responseFuture: Future[ActorRef] = actorRef.mapTo[ActorRef]
            onSuccess(responseFuture) { extraction =>
              complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, extraction.toString()))
            }
          }
        } ~
        path("start" / Remaining) { pathRest =>
          get {
            Patterns.ask(orchestrator, StartService(pathRest), 50000)
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "Service started"))
          }
        } ~
        path("stop" / Remaining) { pathRest =>
          get {
            Patterns.ask(orchestrator, StopService(pathRest), 50000)
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "Service stopped"))
          }
        } ~
        path("state" / Remaining) { pathRest =>
          get {
            val actorRef = Patterns.ask(orchestrator, GetState(pathRest), 50000)
            val responseFuture: Future[Boolean] = actorRef.mapTo[Boolean]
            val result = onSuccess(responseFuture)
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, result.toString))
          }
        } ~
        path("actors") {
          get {
            val actorRef = Patterns.ask(orchestrator, FindAll, 50000)
            val responseFuture: Future[mutable.HashMap[ActorRef, Boolean]] = actorRef.mapTo[mutable.HashMap[ActorRef, Boolean]]
            onSuccess(responseFuture) { extraction =>
              complete {
                List(
                  MicroService(extraction.keySet.toList(0).toString(),extraction.values.toList(0)),
                  MicroService(extraction.keySet.toList(1).toString(),extraction.values.toList(1))
                )
              }
            }
          }
        }
  }

  def setOrchestrator(orchestrator: ActorSelection): Unit ={
      this.orchestrator = orchestrator
  }

}
