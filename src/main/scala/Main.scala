import demo.{TestActor1, TestActor2}
import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}
import core._
import core.api.WebServer

import Roles.{Orchestrator, Seed, TestActor1, TestActor2, WebServer}


object Main extends App {
  val role = args.headOption.getOrElse(Seed)
  val config = ConfigFactory.parseString(s"""akka.cluster.roles = ["$role"]""")
    .withFallback(ConfigFactory.load())

  val app = config.getString("args.app-name")
  val system = ActorSystem(app, config)

  def startMessageGenerator(producer: ActorRef): Unit = {
    import scala.concurrent.duration.DurationInt
    import system.dispatcher
    system.scheduler.schedule(10 seconds, 2 seconds, producer, SimpleMessage)
  }

  role match {
    case Orchestrator =>
      val producer = system.actorOf(Props[RoundRobinProducerActor], Orchestrator)
      startMessageGenerator(producer)
    case TestActor1 =>
      system.actorOf(Props[TestActor1], TestActor1)
    case TestActor2=>
      val testActor2 = system.actorOf(Props[TestActor2], TestActor2)
      system.actorSelection("akka.tcp://AkkaClusterDemo@127.0.0.1:2552/user/orchestrator") ! Register("testActor2",testActor2)
    case WebServer =>
      val webServer = new WebServer()
      webServer.setOrchestrator(system.actorSelection("akka.tcp://AkkaClusterDemo@127.0.0.1:2552/user/orchestrator"))
      webServer.startServer("localhost", 8080)
    case Seed =>
  }

}




  /** def main(args: Array[String]): Unit = {
    if (args.isEmpty)
      startup(Seq("2551", "2552", "4"))
    else
      startup(args)
  }

    val system = ActorSystem("cluster")

    val testActor1 = system.actorOf(Props[TestActor1], name = "testActor1")
    val testActor2 = system.actorOf(Props[TestActor2], name = "testActor2")
    val orchestrator = system.actorOf(Props[Orchestrator], name = "orchestrator")

    orchestrator ! Register("testActor1",testActor1)
    orchestrator ! Register("testActor2",testActor2)

    val system = ActorSystem("OrchestratorSystem", ConfigFactory.load("application"))
    val system2 = ActorSystem("ClusterSystem2", ConfigFactory.load("node2.conf"))
    val system3 = ActorSystem("ClusterSystem3", ConfigFactory.load("node3.conf"))

    val testActor1 = system2.actorOf(Props[TestActor1], name = "testActor1")
    val testActor2 = system3.actorOf(Props[TestActor2], name = "testActor2")
    val orchestrator = system.actorOf(Props[Orchestrator], name = "orchestrator")

    orchestrator ! Register("testActor1",testActor1)
    orchestrator ! Register("testActor2",testActor2)

    val webServer = new WebServer()

    webServer.setOrchestrator(orchestrator)

    webServer.startServer("localhost", 8080)

  def startup(ports: Seq[String]): Unit = {

    ports foreach { port =>
      // Override the configuration of the port
      val config = ConfigFactory.parseString(s"""
        akka.remote.netty.tcp.port=$port
        akka.remote.artery.canonical.port=$port
        persistence {
          journal {
            plugin = "akka.persistence.journal.leveldb-shared"
            leveldb {
              dir = "target/persistence/journal"
            }
          }
          snapshot-store.local.dir = "target/persistence/snapshots"
         }
        """).withFallback(ConfigFactory.load())

      // Create an Akka system
      val system = ActorSystem("ClusterSystem", config)
      val sharedJournal = system.actorOf(Props(new SharedLeveldbStore), "shared-journal")
      SharedLeveldbJournal.setStore(sharedJournal, system)
      // Create an actor that handles cluster domain events
      val orchestrator = system.actorOf(Props[Orchestrator], name = "orchestrator")
      val testActor1 = system.actorOf(Props[TestActor1], name = "testActor1")
      val testActor2 = system.actorOf(Props[TestActor1], name = "testActor2")

      orchestrator ! Register("testActor1",testActor1)
      orchestrator ! Register("testActor2",testActor2)

      val webServer = new WebServer()

      webServer.setOrchestrator(orchestrator)

      webServer.startServer("localhost", 8080)




    }
  }**/


