package morphological.analyzer.servers

import colossus.core.IOSystem
import colossus.core.ServerRef
import colossus.protocols.http.{HttpServer,Initializer}
import morphological.analyzer.services.MorphologicalAnalysisService
object MorphologicalAnalysisServer {
  def start(port: Int)(implicit sys: IOSystem): ServerRef = {
    println("start morphological analysis server")
    HttpServer.start("morphological-analyzer", port) {
      implicit worker => {
        new Initializer(worker) {
          def onConnect = context => new MorphologicalAnalysisService(context)
        }
      }
    }
  }
}