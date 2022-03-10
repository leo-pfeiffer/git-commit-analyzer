package com.github.leopfeiffer.gitcommitanalyzer

import filereader.FileReader
import parser.TxtParser

object Main extends App {
  val content = FileReader.read("src/test/resources/test-gitlog.txt")
  val log = TxtParser.main(content)
}