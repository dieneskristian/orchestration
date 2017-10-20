package core

import akka.actor.Actor

abstract class OrchestratedActor extends Actor{

  val name: String

  def receive = {
    case "name" => println("Actor name: "+name)
    case "startService" => println("Service started")
    case "stopService" => println("Service stopped")
  }

}
