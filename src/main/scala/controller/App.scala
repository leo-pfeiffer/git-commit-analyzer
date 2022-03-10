package com.github.leopfeiffer.gitcommitanalyzer
package controller

import gitlog.{GitLog, Node}

import upickle.default.*

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_DATE_TIME
import filereader.FileReader
import parser.TxtParser
import utils.*

import ujson.Value.Value


object App extends cask.MainRoutes{

  @cask.get("/")
  def hello(): String = {
    "Hello World!"
  }

  /**
   * Test by reading the test git log and returning the JSON version
   * */
  @cask.get("/test")
  def test(): Value = {
    val content = FileReader.read("src/test/resources/test-gitlog.txt")
    val log = TxtParser.main(content)
    ujson.read(write(log))
  }

  /**
   * Parse plain git log to JSON.
   * */
  @cask.post("/parse")
  def parse(request: cask.Request): Value = {
    val log = TxtParser.main(request.text())
    ujson.read(write(log))
  }

  initialize()
}
