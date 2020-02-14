# MorphologicalAnalyzer

- 日本語/韓国語/中国語/英語に対応した形態素解析サービスです。
- 韓国語については別途 org.snu.ids.ha.2.0.jar( https://github.com/konlpy/kkma)が必要になります。どこのリポジトリにも存在しないので手動でダウンロードしてクラスパスに追加する必要があります。また別途辞書ファイルが必要です。
- 中国語/英語はstanford pos tagger / stanford segmenter(中国語のみ）を使用しています。別途辞書ファイルが必要です。
- sbt assembly した後 morphological/target/scala-2.12にjarができるので
java -cp morphorogical-assembly-0.1-SNAPSHOT.jar morphological.analyzer.Main
とやると起動します。
