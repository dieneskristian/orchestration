import demo.{ClusterListenerActor, TestActor1, TestActor2}
import akka.actor.{ActorSystem, Props}
import core.Orchestrator


object Main extends App {

  val system = ActorSystem("CustomActorSystem")

  val testActor1 = system.actorOf(Props[TestActor1], name = "testActor1")
  val orchestrator = system.actorOf(Props[Orchestrator], name = "orchestrator")

  testActor1 ! "name"
  testActor1 ! "specificMessage"
  orchestrator ! testActor1
  orchestrator ! "test"

}
