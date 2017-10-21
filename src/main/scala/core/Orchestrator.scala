package core

import akka.actor.{Actor, ActorRef}
import core.Orchestrator.{FindByName, Register}

import scala.collection.mutable._


class Orchestrator extends Actor{


  val actors = new HashMap[String,ActorRef] with SynchronizedMap[String,ActorRef]

  override def receive = {
    case Register(id,service) =>
      actors.getOrElseUpdate(id,service)
    case FindByName(name) =>
      println(actors.get(name).get ! "name")
  }

}

//companion object
object Orchestrator {

  case class Register(name: String, service: ActorRef)
  case class FindByName(name: String)

}
