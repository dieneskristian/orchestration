package demo

import core.OrchestratedActor

class TestActor2 extends OrchestratedActor{

  override val name = "testActor2"

  override def receive = super.receive orElse {
    case "specificMessage" => println("specific")
  }

}
