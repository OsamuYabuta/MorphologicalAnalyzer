package morphological.analyzer

import akka.actor.ActorSystem
import colossus.core.IOSystem
import morphological.analyzer.servers.MorphologicalAnalysisServer

object Main {
  def main(args:Array[String]) = {
    implicit val actorSys = ActorSystem("morphological-analyzer")
    implicit val ioSystem = IOSystem()

    //start front api http server
    MorphologicalAnalysisServer.start(9100)

    ()
  }
}