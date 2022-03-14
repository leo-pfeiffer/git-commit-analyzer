package com.github.leopfeiffer.gitcommitanalyzer
package routes

import requests.post
import ujson.Value.Value

class GithubRoutes()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes {

  val ghClientId: String = System.getenv("CLIENT_ID")
  val ghClientSecret: String = System.getenv("CLIENT_SECRET")
  val ghBaseUri = "https://github.com/login/oauth/access_token"
  val ghRedirectUri = "http://localhost:8080/_oauth-callback"

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

  initialize()

}
