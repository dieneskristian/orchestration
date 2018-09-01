package core

import akka.actor.ActorRef

final case class Register(name: String, service: ActorRef)

final case class FindByName(name: String)

final case class FindAll()

final case class StartService(id: String)

final case class StopService(id: String)

final case class GetState(id: String)

case object OrchestratorMessages