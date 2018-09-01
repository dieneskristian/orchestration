package demo

import akka.actor.{AbstractActor, Actor, ActorLogging, ActorSystem, Props, ReceiveTimeout}
import core.Register

class TestActor2 extends Actor with ActorLogging{
  override def receive(): Receive = {
    case "name" => sender() ! "testActor2"
    case msg => context.actorSelection("akka.tcp://AkkaClusterDemo@127.0.0.1:2552/user/orchestrator") ! Register("testActor2",self)
  }
}