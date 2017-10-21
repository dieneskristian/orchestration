package core

import akka.actor.{Actor, ActorRef}
import core.Orchestrator.{FindAll, FindByName, Register}

import scala.collection.mutable._


class Orchestrator extends Actor{


  val actors = new HashMap[String,ActorRef] with SynchronizedMap[String,ActorRef]

  override def receive = {
    case Register(id,service) =>
      actors.getOrElseUpdate(id,service)
    case FindByName(name) =>
      println(actors.get(name).get ! "name")
    case FindAll =>
      actors.foreach(actor => actor._2 ! "name")
  }

}

//companion object
object Orchestrator {

  case class Register(name: String, service: ActorRef)
  case class FindByName(name: String)
  case class FindAll()

}
