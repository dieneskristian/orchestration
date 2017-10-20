package core

import akka.actor.{Actor, ActorRef}

import scala.collection.mutable._

class Orchestrator extends Actor{

  val actors = new HashMap[String,ActorRef] with SynchronizedMap[String,ActorRef]

  override def receive = {
    case actor: ActorRef =>
      actors.getOrElseUpdate("test",actor)
    case name: String =>
      println(actors.get(name).get ! "name")
  }

}
