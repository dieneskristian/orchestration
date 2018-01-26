package demo

import akka.actor.Actor

class TestActor2 extends Actor{

  override def receive = {
    case "specificMessage" => println("specific")
  }

}
