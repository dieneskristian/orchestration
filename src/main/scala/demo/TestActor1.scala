package demo

import akka.actor.Actor
import core.ActorDecorator.StartService
import core.ActorDecorator.StopService

class TestActor1 extends Actor {

  val name = "testActor1"

  override def receive = {
    case "specificMessage" => println("specific")
    case "start" => StartService()
    case "stop" => StopService()
  }


}
