package com.github.leopfeiffer.gitcommitanalyzer
package controller

import filereader.FileReader
import parser.TxtParser
import utils.formats

import org.json4s._
import org.json4s.jackson.Serialization.{read, write}


object App extends cask.MainRoutes{
  @cask.get("/")
  def hello(): String = {
    "Hello World!"
  }

  // todo check how the getJson works and see how we can use ujson
  @cask.get("/test")
  def test(): String = {
    val content = FileReader.read("src/test/resources/test-gitlog.txt")
    val log = TxtParser.main(content)
    write(log)
  }

  @cask.post("/do-thing")
  def doThing(request: cask.Request): String = {
    request.text().reverse
  }

  initialize()
}
