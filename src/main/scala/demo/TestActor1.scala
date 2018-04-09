package demo

import akka.actor.{AbstractActor, Actor, ActorLogging, ActorSystem, Props}
import core.Register

class TestActor1 extends Actor with ActorLogging{
  override def receive(): Receive = {
    case msg => context.actorSelection("akka.tcp://AkkaClusterDemo@127.0.0.1:2552/user/orchestrator") ! Register("testActor1",self)
  }
}