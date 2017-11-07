package core

import akka.actor.ActorRef

class ActorDecorator(val actor: ActorRef) {

  def startService = println("Service " + actor + "started")
  def stopService = println("Service " + actor + "stopped")

}
