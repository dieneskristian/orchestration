package core

import akka.actor.Actor
import com.typesafe.config.ConfigFactory

class ConfigurationProvider extends Actor{

  override def receive = {
    case GetConfig(port) =>
      sender() ! ConfigFactory.parseString(s"""
        akka.remote.netty.tcp.port=$port
        akka.remote.artery.canonical.port=$port
        """).withFallback(ConfigFactory.load())

  }
}
