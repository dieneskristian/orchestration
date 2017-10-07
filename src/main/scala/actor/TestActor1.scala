package actor

import akka.actor.Actor

class TestActor1 extends Actor {

  def receive = {
    case "greetings" => println("Hello There!")
    case "test" =>
      val result = "welcome"
      sender ! result
    case _ => println("Cannot parse your message yet")
  }



}
