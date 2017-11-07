package demo

import akka.actor.Actor

class TestActor2 extends Actor {

  val name = "testActor2"

  override def receive = {
    case "specificMessage" => println("specific")
  }

}
