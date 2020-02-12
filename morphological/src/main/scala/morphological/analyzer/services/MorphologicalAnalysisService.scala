package morphological.analyzer.services

import akka.util.Timeout
import colossus.core.ServerContext
import colossus.protocols.http.HttpMethod._
import colossus.protocols.http.UrlParsing._
import colossus.protocols.http.{RequestHandler, _}
import colossus.service.Callback

import scala.concurrent.duration._

import morphological.analyzer.analyzers._
import morphological.analyzer.analyzers.JsonConversionProtocol._
import spray.json._
import akka.util.ByteString
import scala.util.{Try,Success,Failure}

class MorphologicalAnalysisService(
  context:ServerContext
) extends RequestHandler(context) {
  private implicit val timeout: Timeout = 3.0 seconds

  def handle = {
    case req@Post on Root / "ja"  => {
        val headers = HttpHeaders(HttpHeader(
          "Content-Type" , "application/json; charset=utf8"
        ))
        val textOpt = pickupText(req.body.bytes)
        val analyzer = JapaneseAnalyzer()
        analyzer.parse(textOpt) match {
          case Success(result) => Callback.successful(req.respond(HttpCodes.OK, result.toJson.prettyPrint, headers))
          case Failure(e)  => e.printStackTrace();Callback.successful(req.respond(HttpCodes.OK, "{}", headers))
        }
    }
    case req@Post on Root / "ko" => {
      val headers = HttpHeaders(HttpHeader(
        "Content-Type" , "application/json; charset=utf8"
      ))
      val textOpt = pickupText(req.body.bytes)
      val analyzer = KoreanAnalyzer()
      analyzer.parse(textOpt) match {
        case Success(result) => Callback.successful(req.respond(HttpCodes.OK, result.toJson.prettyPrint, headers))
        case Failure(e) => e.printStackTrace();Callback.successful(req.respond(HttpCodes.SERVICE_UNAVAILABLE, "{}", headers))
      }
    }
    case req@Post on Root / "cn" => {
      val headers = HttpHeaders(HttpHeader(
        "Content-Type" , "application/json; charset=utf8"
      ))
      val textOpt = pickupText(req.body.bytes)
      val analyzer = ChineseAnalyzer()
      analyzer.parse(textOpt) match {
        case Success(result) => Callback.successful(req.respond(HttpCodes.OK, result.toJson.prettyPrint, headers))
        case Failure(e) => e.printStackTrace();Callback.successful(req.respond(HttpCodes.SERVICE_UNAVAILABLE, "{}", headers))
      }
    }
    case req@Post on Root / "en" => {
      val headers = HttpHeaders(HttpHeader(
        "Content-Type" , "application/json; charset=utf8"
      ))
      val textOpt = pickupText(req.body.bytes)
      val analyzer = ChineseAnalyzer()
      analyzer.parse(textOpt) match {
        case Success(result) => Callback.successful(req.respond(HttpCodes.OK, result.toJson.prettyPrint, headers))
        case Failure(e) => e.printStackTrace();Callback.successful(req.respond(HttpCodes.SERVICE_UNAVAILABLE, "{}", headers))
      }
    }
  }

  private def pickupText(bytes:ByteString):Option[String] =  {
    val bodyString = new String(bytes.toArray , "UTF-8")
    bodyString.split("&").map(p => {
      val keyValue = p.split("=")
      (keyValue.lift(0).getOrElse("") , keyValue.lift(1).getOrElse(""))
    }).filter(_._1 == "text").map(_._2).headOption
  }
}
