package demo

import core.OrchestratedActor

class TestActor1 extends OrchestratedActor {

  override val name = "testActor1"

  override def receive = super.receive orElse {
    case "specificMessage" => println("specific")
  }



}
