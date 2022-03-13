package com.github.leopfeiffer.gitcommitanalyzer
package controller

import gitlog.{GitLog, Node}

import upickle.default.*

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_DATE_TIME
import filereader.FileReader
import dao.*
import parser.TxtParser
import utils.*

import requests.post

import ujson.Value.Value

object App extends cask.MainRoutes{

  override def port: Int = 9000

  val ghClientId: String = System.getenv("CLIENT_ID")
  val ghClientSecret: String = System.getenv("CLIENT_SECRET")
  val ghBaseUri = "https://github.com/login/oauth/access_token"
  val ghRedirectUri = "http://localhost:8080/_oauth-callback"

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

  // GITHUB AUTH FLOW
  @cask.get("/gh-login")
  def ghLogin(request: cask.Request): Unit = {
    val url = s"$ghBaseUri?client_id=$ghClientId&redirect_uri=$ghRedirectUri"

    cask.Redirect(url)
  }

  @cask.get("/_oauth-callback")
  def oAuthCallback(code: String): Value = {

    val url = s"$ghBaseUri?client_id=$ghClientId&redirect_uri=$ghRedirectUri&client_secret=$ghClientSecret&code=$code"
    val r = post(url, headers = Map("accept" -> "application/json"))

    val obj: Value = ujson.read(r.data.toString)

    // todo do sth like this instead: https://stackoverflow.com/a/55063199
    try
      println(obj("access_token"))
      obj("access_token")
    catch
      case e: Exception =>
        println(obj("error"))
        obj("error")
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

  // ACTUAL API ROUTES ========

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

  @cask.get("/gitlog")
  def getAllGitlog(request: cask.Request): Value = {
    ujson.Arr(ujson.Obj("gitlogId" -> 1))
  }

  @cask.post("/gitlog")
  def createGitlog(request: cask.Request): Value = {
    val log = TxtParser.main(request.text())
    ujson.read(write(log))
  }

  initialize()
}
