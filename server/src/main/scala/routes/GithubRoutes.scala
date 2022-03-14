package com.github.leopfeiffer.gitcommitanalyzer
package routes

import cask.Cookie
import requests.{get, post}
import ujson.Value.Value

class GithubRoutes()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes {

  val ghClientId: String = System.getenv("CLIENT_ID")
  val ghClientSecret: String = System.getenv("CLIENT_SECRET")
  val ghAuthorizeUri = "https://github.com/login/oauth/authorize"
  val ghAccessTokenUri = "https://github.com/login/oauth/access_token"
  val ghScope = "scope=user%20repo:status"
  val ghApiBaseUri = "https://api.github.com"
  val ghRedirectUri = "http://localhost:9000/_oauth-callback"

  // GITHUB AUTH FLOW
  @cask.get("/gh-login")
  def ghLogin(request: cask.Request) = {
    val url = s"$ghAuthorizeUri?client_id=$ghClientId&redirect_uri=$ghRedirectUri&scope=$ghScope"
    cask.Redirect(url)
  }

  // todo
  @cask.get("/gh-logout")
  def ghLogout(request: cask.Request) = {
    "logged out"
  }

  @cask.get("/_oauth-callback")
  def oAuthCallback(code: String) = {

    val url = s"$ghAccessTokenUri?client_id=$ghClientId" +
      s"&redirect_uri=$ghRedirectUri&client_secret=$ghClientSecret&code=$code"

    val r = post(url, headers = Map("accept" -> "application/json"))

    val obj: Value = ujson.read(r.data.toString)

    // todo do sth like this instead: https://stackoverflow.com/a/55063199
    try
      println("Cookie set: " + obj("access_token"))
      cask.Response(
        "Cookies Set!",
        cookies = Seq(cask.Cookie("GtaGhAuthToken", obj("access_token").value.toString))
      )
    catch
      case e: Exception =>
        println(obj("error"))
        cask.Response("Error :(")
  }

  @cask.get("/gh-token")
  def ghToken(GtaGhAuthToken: cask.Cookie): Value = {
    try
      val token = GtaGhAuthToken.value
      ujson.Obj("token" -> token)
    catch
      case e: Exception =>
        println("Cookie not found")
        ujson.Obj("Error" -> "Token not found. Authenticate via /gh-login first.")
  }

  @cask.route("/github/repos", methods = Seq("get"))
  def githubRepos(request: cask.Request): Value = {

    val token = request.headers("authorization").head

    // todo how to handle multiple pages?
    //  idea: repeat until all fetched: get page, append results
    val url = s"$ghApiBaseUri/user/repos"
    val r = get(url, headers = Map(
      "accept" -> "application/vnd.github.v3+json",
      "authorization" -> s"$token"
    ))

    ujson.read(r.data.toString)

    // todo map only names and return
  }

  @cask.route("/github/repos", methods = Seq("options"))
  def cors(request: cask.Request) = {
    "allow_cors"
  }

  @cask.get("/github/commits/:org/:repo")
  def githubCommitsForRepo(org: String, repo: String): Value = {

    // todo: again, handle multiple pages
    val url = s"$ghApiBaseUri/repos/${org}/${repo}/commits?type=all&sort=full_name"
    val r = get(url, headers = Map("accept" -> "application/vnd.github.v3+json"))

    ujson.read(r.data.toString)

    // todo: convert to Gitlog and return

    // todo: problem, response does not include stats
    //  -> to get stats, need to call GET /repos/{owner}/{repo}/commits/{ref} on every commit
    //  -> will be VERY time consuming
    //  -> single api call to endpoint is ~ 50-300 ms... for 100 commits that's already 30s

  }

  initialize()

}
