package core

import akka.actor.{Actor, ActorRef, PoisonPill}
import core.Orchestrator._

import scala.collection.mutable._


class Orchestrator extends Actor{

  val actors = new HashMap[String,ActorRef]
  val actorsStates =  new HashMap[ActorRef,Boolean]

  override def receive = {
    case Register(id,service) =>
      actors.getOrElseUpdate(id,service)
      actorsStates.getOrElseUpdate(service,true)
    case FindByName(name) =>
      sender() ! actors.get(name).get
    case FindAll =>
      sender() ! actors
    case StartService(id) =>
      //actors.get(id).get.tell(Start.,this.sender())
      actorsStates.update(actors.get(id).get,true)
    case StopService(id) =>
      actors.get(id).get.tell(PoisonPill.getInstance,this.sender())
      actorsStates.update(actors.get(id).get,false)
    case GetState(id) =>
      sender() ! actorsStates.get(actors.get(id).get).get
  }
}


//companion object
object Orchestrator {

  case class Register(name: String, service: ActorRef)
  case class FindByName(name: String)
  case class FindAll()
  case class StartService(id: String)
  case class StopService(id: String)
  case class GetState(id: String)

}

