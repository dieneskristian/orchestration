package demo

import akka.actor.Actor

class TestActor1 extends Actor {

  val name = "testActor1"

  override def receive = {
    case "specificMessage" => println("specific");
  }


}
