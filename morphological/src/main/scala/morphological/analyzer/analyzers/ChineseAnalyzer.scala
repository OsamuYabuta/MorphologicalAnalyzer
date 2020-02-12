package morphological.analyzer.analyzers

import java.io.StringReader
import scala.collection.JavaConverters._
import edu.stanford.nlp.tagger.maxent.MaxentTagger
import edu.stanford.nlp.ie.crf.CRFClassifier
import java.util.Properties
import scala.util.Try

object ChineseAnalyzer extends App {
  private lazy val baseDir = "/usr/local/lib/stanfordsegmenter"
  private lazy val tokenizer = new MaxentTagger("/usr/local/lib/stanfordpostagger/models/chinese-distsim.tagger")
  private lazy val props = getSegmenterProps()
  private lazy val segmenter = {
    val segmenter = new CRFClassifier(props)
    segmenter.loadClassifierNoExceptions(baseDir + "/ctb.gz", props)
    segmenter
  }

  def apply() = {
    new ChineseAnalyzerImpl(tokenizer,segmenter)
  }

  private def getSegmenterProps(): Properties = {
    val props = new Properties()
    props.setProperty("sighanCorporaDict", baseDir)
    props.setProperty("serDictionary", baseDir + "/dict-chris6.ser.gz")
    props.setProperty("inputEncoding", "UTF-8")
    props.setProperty("sighanPostProcessing", "true")

    props
  }

  //for only test
  delayedInit({
    val text = args.lift(0).getOrElse("none")
    println(ChineseAnalyzer().parse(Option(text)))
  })
}

class ChineseAnalyzerImpl(tokenizer:MaxentTagger,segmenter:CRFClassifier[_]) extends LanguageAnalyzerSupport {
  def parse(textOpt:Option[String]):Try[AnalyzedResults]  = {
    Try(textOpt.fold(AnalyzedResults(List())) {
      text => {
        distinct(AnalyzedResults(segmenter.segmentString(text).asScala.toList.map {
          segment => {
            (MaxentTagger.tokenizeText(new StringReader(segment)).asScala.toList.map {
              tokenizedSegments => tokenizer.tagSentence(tokenizedSegments).asScala.toList.map {
                taggedWord => AnalyzedResult(
                  taggedWord.word(),
                  taggedWord.tag(),
                  1
                )
              }
            }).flatten
          }
        } flatten))
      }
    })
  }
}