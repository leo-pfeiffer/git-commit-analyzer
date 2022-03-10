package com.github.leopfeiffer.gitcommitanalyzer
package controller

import gitlog.{GitLog, Node}

import upickle.default.*

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_DATE_TIME
import filereader.FileReader
import parser.TxtParser
import utils.*

import requests.post

import ujson.Value.Value

object App extends cask.MainRoutes{

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
  def ghLogin() = {
    val url = s"$ghBaseUri?client_id=$ghClientId&redirect_uri=$ghRedirectUri"

    cask.Redirect(url)
  }

  @cask.get("/_oauth-callback")
  def oAuthCallback(code: String) = {

    val url = s"$ghBaseUri?client_id=$ghClientId&redirect_uri=$ghRedirectUri&client_secret=$ghClientSecret&code=$code"
    val r = post(url, headers = Map("accept" -> "application/json"))

    val obj: Value = ujson.read(r.data.toString)

    // todo do sth like this instead: https://stackoverflow.com/a/55063199
    try
      println(obj("access_token"))
      obj("access_token")
    catch
      case e: Exception => {
        println(obj("error"))
        obj("error")
      }

  }

  initialize()
}
