import demo.{TestActor1, TestActor2}
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.routing.Router
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import core.Orchestrator
import core.Orchestrator.Register
import core.api.WebServer


object Main extends App {

   val system = ActorSystem("CustomActorSystem")

  val testActor1 = system.actorOf(Props[TestActor1], name = "testActor1")
  val testActor2 = system.actorOf(Props[TestActor2], name = "testActor2")
  val orchestrator = system.actorOf(Props[Orchestrator], name = "orchestrator")

  testActor1 ! "name"
  testActor1 ! "specificMessage"
  orchestrator ! Register("testActor1",testActor1)
  orchestrator ! Register("testActor2",testActor2)

  val webServer = new WebServer()

  webServer.setOrchestrator(orchestrator)

  webServer.startServer("localhost", 8080)

}
