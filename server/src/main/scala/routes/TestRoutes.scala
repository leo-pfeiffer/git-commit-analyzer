package com.github.leopfeiffer.gitcommitanalyzer
package routes

import dao.*
import utils.*
import filereader.FileReader
import parser.TxtParser

import ujson.Value.Value
import upickle.default.write

class TestRoutes()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes {

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


  @cask.get("/delete/:user")
  def deleteUser(user: Int): String = {
    Dao.deleteUser(user) match {
      case Fail(msg) => s"This didn't work: $msg"
      case Success(_, _) => "Yay!"
    }
  }

  @cask.get("/create")
  def createUser(): String = {
    Dao.insertUser("leo@me.com") match {
      case Fail(msg) => s"This didn't work: $msg"
      case Success(_, _) => "Yay!"
    }
  }

  @cask.get("/get-user/:user")
  def getUserById(user: Int): String = {
    Dao.findUser(user) match {
      case Fail(msg) => s"This didn't work: $msg"
      case Success(_, data: String) => s"User: $data"
      case _ => "This shouldn't happen"
    }
  }
  
  initialize()

}
