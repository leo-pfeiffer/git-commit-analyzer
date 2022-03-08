package com.github.leopfeiffer.gitcommitanalyzer

import filereader.FileReader
import parser.TxtParser

import org.json4s._
import org.json4s.jackson.Serialization.{read,write}

import utils.formats

object Main extends App {
  val content = FileReader.read("src/test/resources/test-gitlog.txt")
  val log = TxtParser.main(content)

  println(write(log))
}