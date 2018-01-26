package demo

import akka.actor.Actor

class TestActor1 extends Actor{

  override def receive = {
    case "specificMessage" => println("specific");
  }


}
