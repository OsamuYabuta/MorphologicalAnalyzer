package morphological.analyzer.analyzers

import com.atilika.kuromoji.ipadic.Token
import com.atilika.kuromoji.ipadic.Tokenizer
import scala.collection.JavaConverters._
import scala.util.Try
import spray.json._
import DefaultJsonProtocol._

case class AnalyzedResult(
 keyword:String,
 tag:String,
 tf:Int
)
case class AnalyzedResults (values:Seq[AnalyzedResult])

object JsonConversionProtocol {
  implicit val analyzedResult      = jsonFormat3(AnalyzedResult)
  implicit val anlyzedResultFormat = jsonFormat1(AnalyzedResults)
}

trait LanguageAnalyzerSupport {
  def distinct(analyzedResults:AnalyzedResults):AnalyzedResults = {
    analyzedResults.copy(values = analyzedResults.values.groupBy {
      analyzedResult => analyzedResult.keyword
    } flatMap {
      case (k , v) => {
        v.headOption match {
          case Some(analyzedResult) => Some(analyzedResult.copy(tf = v.size))
          case _ => None
        }
      }
    } toList)
  }
}

object JapaneseAnalyzer extends App{
  private lazy val tokenizer = new Tokenizer()
  def apply() = {
    new JapaneseAnalyzerImpl(tokenizer)
  }

  //for only test
  delayedInit({
    val text = args.lift(0).getOrElse("none")
    println(JapaneseAnalyzer().parse(Option(text)).getOrElse(AnalyzedResults(List())).values.map(k => k.keyword + "/" + k.tag).mkString("\n"))
  })
}

class JapaneseAnalyzerImpl(val tokenizer: Tokenizer) extends LanguageAnalyzerSupport {
  def parse(textOpt:Option[String]):Try[AnalyzedResults] = {
    Try(textOpt.fold(AnalyzedResults(List())) {
      text => {
        val tokenized:List[Token] = tokenizer.tokenize(text).asScala.toList
        distinct(AnalyzedResults(tokenized.map {
          token => AnalyzedResult(
            token.getSurface,
            token.getAllFeatures,
            1
          )
        }))
      }
    })
  }
}