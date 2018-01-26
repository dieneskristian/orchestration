package core

import akka.actor.SupervisorStrategy.{Restart, Stop}
import akka.actor.{ActorRef, Kill}


class ActorDecorator(val actor: ActorRef) {

  def startService(): Unit ={
    System.out.println("started")
  }

  def stopService(): Unit ={
    System.out.println("stopped")
  }

}
