package core.api


import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{HttpApp, Route}
import akka.pattern.Patterns
import akka.testkit.TestActor
import core.{FindByName, GetState, StartService, StopService}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class WebServer extends HttpApp{

  var orchestrator: ActorRef = null

  override def routes(): Route = {


    path("actor" / Remaining) { pathRest =>
      get {
        val actorRef = Patterns.ask(orchestrator, FindByName(pathRest), 50000)
        val responseFuture: Future[ActorRef] = actorRef.mapTo[ActorRef]
        val result = Await.result(responseFuture, Duration.create(500, "millis"))
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, result.toString()))
      }
    }~
      path("start" / Remaining) { pathRest =>
      get {
        Patterns.ask(orchestrator, StartService(pathRest), 50000)
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "Service started"))
      }
    }~
      path("stop" / Remaining) { pathRest =>
        get {
          Patterns.ask(orchestrator, StopService(pathRest), 50000)
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "Service stopped"))
        }
      }~
      path("state" / Remaining) { pathRest =>
      get {
        val actorRef = Patterns.ask(orchestrator, GetState(pathRest), 50000)
        val responseFuture: Future[Boolean] = actorRef.mapTo[Boolean]
        val result = Await.result(responseFuture, Duration.create(500, "millis"))
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, result.toString))
      }
    }~
      path("test" / Remaining) { pathRest =>
        get {
          val actorRef = Patterns.ask(orchestrator, FindByName(pathRest), 50000)
          val responseFuture: Future[ActorRef] = actorRef.mapTo[ActorRef]
          val actor = Await.result(responseFuture, Duration.create(500, "millis"))
          val stringResult = Patterns.ask(actor, "specificMessage" , 50000)
          val stringFuture : Future[String] = stringResult.mapTo[String]
          val result  = Await.result(stringFuture, Duration.create(500, "millis"))
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, result))
        }
      }

  }

  /**
  override def routes(): Route = path("start" / Remaining) { pathRest =>
    get {
      orchestrator ! StartService(pathRest)
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,"service started"))
    }
  } **/



  def setOrchestrator(orchestrator: ActorRef): Unit ={
      this.orchestrator = orchestrator
  }

}
