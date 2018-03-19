import demo.{TestActor1, TestActor2}
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.Patterns
import com.typesafe.config.{Config, ConfigFactory}
import core._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration


object Main {

  /**val system = ActorSystem("OrchestratorSystem", ConfigFactory.load("application"))
  val system2 = ActorSystem("ClusterSystem2", ConfigFactory.load("node2.conf"))
  val system3 = ActorSystem("ClusterSystem3", ConfigFactory.load("node3.conf"))

  val testActor1 = system1.actorOf(Props[TestActor1], name = "testActor1")
  val testActor2 = system2.actorOf(Props[TestActor2], name = "testActor2")
  val orchestrator = system.actorOf(Props[Orchestrator], name = "orchestrator")

  orchestrator ! Register("testActor1",testActor1)
  orchestrator ! Register("testActor2",testActor2)

  val webServer = new WebServer()

  webServer.setOrchestrator(orchestrator)

  webServer.startServer("localhost", 8080) **/

  def main(args: Array[String]): Unit = {
    if (args.isEmpty)
      startup(Seq("2551", "2552", "0"))
    else
      startup(args)
  }

  def startup(ports: Seq[String]): Unit = {
    val configProviderSystem = ActorSystem("ClusterSystem");
    val configProvider = configProviderSystem.actorOf(Props[ConfigurationProvider], name = "configProvider")
    ports foreach { port =>
      // Override the configuration of the port
      val configAsker = Patterns.ask(configProvider, GetConfig(port), 50000)
      val responseFuture: Future[Config] = configAsker.mapTo[Config]
      val result = Await.result(responseFuture, Duration.create(500, "millis"))

      // Create an Akka system
      val system = ActorSystem("ClusterSystem", result)
      // Create an actor that handles cluster domain events
      val orchestrator = system.actorOf(Props[Orchestrator], name = "orchestrator")
      val testActor1 = system.actorOf(Props[TestActor1], name = "testActor1")
      val testActor2 = system.actorOf(Props[TestActor1], name = "testActor2")

      orchestrator ! Register("testActor1",testActor1)
      orchestrator ! Register("testActor2",testActor2)

      //val webServer = new WebServer()

      //webServer.setOrchestrator(orchestrator)

      //webServer.startServer("localhost", 8080)
    }
  }


}

