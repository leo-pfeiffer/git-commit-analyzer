package com.github.leopfeiffer.gitcommitanalyzer

import converter.TxtConverter
import filereader.FileReader

object Main extends App {
  val content = FileReader.read("src/main/resources/gitlog.txt")
  val log = TxtConverter.main(content)
  log.log.foreach(println)
}
