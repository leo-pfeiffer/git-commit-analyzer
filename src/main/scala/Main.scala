package com.github.leopfeiffer.gitcommitanalyzer

import parser.TxtParser
import filereader.FileReader

object Main extends App {
  val content = FileReader.read("src/main/resources/gitlog.txt")
  val log = TxtParser.main(content)
  log.log.foreach(println)
}
