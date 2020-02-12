package morphological.analyzer.analyzers

import org.snu.ids.ha.index.KeywordExtractor

import scala.collection.JavaConverters._
import scala.util.Try

object KoreanAnalyzer extends App {
  private lazy val ke = new KeywordExtractor()
  def apply() = {
    new KoreanAnalyzerImpl(ke)
  }

  //for only test
  delayedInit({
    val text = args.lift(0).getOrElse("none")
    println(KoreanAnalyzer().parse(Option(text)).getOrElse(AnalyzedResults(List())).values.map(k => k.keyword + "/" + k.tag).mkString("\n"))
  })
}

class KoreanAnalyzerImpl(val ke:KeywordExtractor) {
    def parse(textOpt:Option[String]):Try[AnalyzedResults] = {
      Try(textOpt.fold(AnalyzedResults(List())) {
        text => {
          val keywordListObj = ke.extractKeyword(text, false)
          AnalyzedResults((0 to keywordListObj.size()-1).toList.map {
            i => {
              val keyword = keywordListObj.get(i)
              AnalyzedResult(
                keyword.getString(),
                keyword.getTag(),
                keyword.getCnt()
              )
            }
          })
        }
      })
    }
}