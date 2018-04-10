package core

import java.util.function.Consumer

import akka.actor.{Actor, ActorIdentity, ActorLogging, ActorRef, PoisonPill, RootActorPath, Terminated}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.persistence.journal.leveldb.SharedLeveldbJournal
import akka.persistence.{PersistentActor, SnapshotOffer}

import scala.util.{Failure, Success, Try}
import scala.collection.mutable._
import Roles.{TestActor1, TestActor2}


object ProducerActor {

  trait RouterStrategy {
    def addRoutee(ref: ActorRef): Unit;
    def removeRoutee(ref: ActorRef): Unit;
    def sendMessage[M >: Message](msg: M): Unit;
  }
}

trait Orchestrator extends Actor with ActorLogging{

  val strategy: ProducerActor.RouterStrategy
  val actors = new HashMap[String,ActorRef]
  val actorsStates =  new HashMap[ActorRef,Boolean]
  val snapshots = new HashMap[String,ActorRef]
  var counter = 0

  val cluster = Cluster(context.system)

  override def preStart(): Unit = cluster.subscribe(self, classOf[MemberUp])
  override def postStop(): Unit = cluster.unsubscribe(self)

  def registerConsumer(refTry: Try[ActorRef]): Unit = refTry match {
    case Success(ref) =>
      context watch ref
      strategy.addRoutee(ref)
    case Failure(_) => log.error("Couldn't find consumer on path!")
  }

  override def receive = {
    case MemberUp(member) if (member.roles.contains(TestActor1)) =>
      log.info(s"""Received member up event for ${member.address}""")
      val testActorRootPath = RootActorPath(member.address)
      val testActorSelection1 = context.actorSelection(testActorRootPath / "user" / TestActor1)

      import scala.concurrent.duration.DurationInt
      import context.dispatcher
      testActorSelection1.resolveOne(5.seconds).onComplete(registerConsumer)
    case Terminated(actor) => strategy.removeRoutee(actor)
    case SimpleMessage =>
      strategy.sendMessage(s"#$counter")
      counter += 1
    case Register(id,service) =>
      if(!actors.contains(id)) {
        actors.update(id, service)
        actorsStates.update(service, true)
        log.info("Registered actor " + id)
      }
    case FindByName(name) =>
      sender() ! actors.get(name).get
    case FindAll =>
      sender() ! actorsStates
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


