package core

import akka.actor.{Actor, ActorRef}
import core.Orchestrator._

import scala.collection.mutable._


class Orchestrator extends Actor{

  val actors = new HashMap[String,ActorRef]

  implicit def decorateActor(service: ActorRef) = new ActorDecorator(service)

  override def receive = {
    case Register(id,service) =>
      actors.getOrElseUpdate(id,service)
    case FindByName(name) =>
      println(actors.get(name).get ! "name")
    case FindAll =>
      actors.foreach(actor => actor._2 ! "name")
    case StartService(id) =>
      actors.get(id).get.startService
    case StopService(id) =>
      actors.get(id).get.stopService
  }
}


//companion object
object Orchestrator {

  case class Register(name: String, service: ActorRef)
  case class FindByName(name: String)
  case class FindAll()
  case class StartService(id: String)
  case class StopService(id: String)

}

