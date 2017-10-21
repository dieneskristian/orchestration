import demo.{TestActor1, TestActor2}
import akka.actor.{ActorSystem, Props}
import core.Orchestrator
import core.Orchestrator.{FindAll, FindByName, Register}


object Main extends App {

  val system = ActorSystem("CustomActorSystem")

  val testActor1 = system.actorOf(Props[TestActor1], name = "testActor1")
  val testActor2 = system.actorOf(Props[TestActor2], name = "testActor2")
  val orchestrator = system.actorOf(Props[Orchestrator], name = "orchestrator")

  testActor1 ! "name"
  testActor1 ! "specificMessage"
  orchestrator ! Register("testActor1",testActor1)
  orchestrator ! Register("testActor2",testActor2)
  orchestrator ! FindAll

}
