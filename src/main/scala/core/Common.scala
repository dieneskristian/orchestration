package core

object Roles {
  val Seed = "seed"
  val Orchestrator = "orchestrator"
  val TestActor1 = "test1"
  val TestActor2 = "test2"
  val WebServer = "server"
}

sealed trait Message
case class SimpleMessage() extends Message
