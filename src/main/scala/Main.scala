import demo.{TestActor1, TestActor2}
import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import core.Orchestrator
import core.Orchestrator.{Register, StopService}
import core.api.WebServer


object Main extends App {

  val system1 = ActorSystem("ClusterSystem1", ConfigFactory.load("node1.conf"))
  val system2 = ActorSystem("ClusterSystem2", ConfigFactory.load("node2.conf"))
  val system3 = ActorSystem("ClusterSystem3", ConfigFactory.load("node3.conf"))

  val testActor1 = system1.actorOf(Props[TestActor1], name = "testActor1")
  val testActor2 = system2.actorOf(Props[TestActor2], name = "testActor2")
  val orchestrator = system3.actorOf(Props[Orchestrator], name = "orchestrator")

  orchestrator ! Register("testActor1",testActor1)
  orchestrator ! Register("testActor2",testActor2)

  val webServer = new WebServer()

  webServer.setOrchestrator(orchestrator)

  webServer.startServer("localhost", 8080)

}
