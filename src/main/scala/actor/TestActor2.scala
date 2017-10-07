package actor

import akka.actor.{Actor, ActorRef}

class TestActor2(testActor1: ActorRef) extends Actor{

  def receive = {
    case "welcome" => testActor1 ! "greetings"
    case _ => println("Cannot parse your message yet")
  }

}
