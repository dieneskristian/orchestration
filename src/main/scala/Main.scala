import demo.TestActor1
import akka.actor.{ActorSystem, Props}
import core.Orchestrator
import core.Orchestrator.{FindByName, Register}


object Main extends App {

  val system = ActorSystem("CustomActorSystem")

  val testActor1 = system.actorOf(Props[TestActor1], name = "testActor1")
  val orchestrator = system.actorOf(Props[Orchestrator], name = "orchestrator")

  testActor1 ! "name"
  testActor1 ! "specificMessage"
  orchestrator ! Register("testActor1",testActor1)
  orchestrator ! FindByName("testActor1")

}
