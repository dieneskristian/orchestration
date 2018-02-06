package core

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._


import scala.collection.mutable._


class Orchestrator extends Actor with ActorLogging{

  val actors = new HashMap[String,ActorRef]
  val actorsStates =  new HashMap[ActorRef,Boolean]

  val cluster = Cluster(context.system)

  // subscribe to cluster changes, re-subscribe when restart
  override def preStart(): Unit = {
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent], classOf[UnreachableMember])
  }
  override def postStop(): Unit = cluster.unsubscribe(self)

  override def receive = {
    case MemberUp(member) =>
      log.info("Member is Up: {}", member.address)
    case UnreachableMember(member) =>
      log.info("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}",
        member.address, previousStatus)
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


