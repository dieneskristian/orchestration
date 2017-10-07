import actor.{TestActor1, TestActor2}
import akka.actor.{ActorSystem, Props}

import com.typesafe.config.ConfigFactory

object Main extends App {

  val system = ActorSystem("CustomActorSystem")

  val testActor1 = system.actorOf(Props[TestActor1], name = "testActor1")
  val testActor2 = system.actorOf(Props(new TestActor2(testActor1)), name = "testActor2")

  testActor2 ! "welcome"
}
