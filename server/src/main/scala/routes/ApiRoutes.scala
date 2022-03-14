package com.github.leopfeiffer.gitcommitanalyzer
package routes

import filereader.FileReader
import parser.TxtParser
import utils.*

import ujson.Value.Value
import upickle.default.*

class ApiRoutes()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes {

  @cask.get("/user/:email")
  def getUser(email: String): String = {
    email
  }

  @cask.postJson("/user")
  def newUser(request: cask.Request): Value = {
    ujson.Obj("email" -> "a@b.c")
  }

  @cask.get("/gitlog/:gitlogId")
  def getGitlogById(gitlogId: Int): Value = {
    ujson.Obj("gitlogId" -> gitlogId)
  }

  @cask.route("/gitlog", methods = Seq("get"))
  def getAllGitlog(request: cask.Request): Value = {
    ujson.Arr(ujson.Obj("gitlogId" -> 1))
  }

  @cask.route("/gitlog", methods = Seq("post"))
  def createGitlog(request: cask.Request): Value = {
    val log = TxtParser.main(request.text())
    ujson.read(write(log))
  }

  initialize()
}
