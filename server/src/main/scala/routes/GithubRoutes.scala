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
  val cookieName = "GtaGhAuthToken"

  // GITHUB AUTH FLOW
  @cask.get("/github/login")
  def ghLogin(request: cask.Request) = {
    val url = s"$ghAuthorizeUri?client_id=$ghClientId&redirect_uri=$ghRedirectUri&scope=$ghScope"
    cask.Redirect(url)
  }

  @cask.route("/github/login", methods = Seq("options"))
  def corsGhLogin(request: cask.Request) = {"allow_cors"}

  // todo this doesn't work yet
  @cask.get("/github/logout")
  def ghLogout() = {
    cask.Response(
      "Cookies Deleted!",
      cookies = Seq(cask.Cookie(cookieName, "", expires = java.time.Instant.EPOCH))
    )
  }

  @cask.get("/_oauth-callback")
  def oAuthCallback(code: String) = {

    val url = s"$ghAccessTokenUri?client_id=$ghClientId" +
      s"&redirect_uri=$ghRedirectUri&client_secret=$ghClientSecret&code=$code"

    val r = post(url, headers = Map("accept" -> "application/json"))

    val obj: Value = ujson.read(r.data.toString)

    // todo do sth like this instead: https://stackoverflow.com/a/55063199
    try
      cask.Response(
        "Cookies Set!",
        cookies = Seq(cask.Cookie(cookieName, obj("access_token").value.toString))
      )
    catch
      case e: Exception =>
        println(obj("error"))
        cask.Response("Error :(")
  }

  /**
   * Retrieve the token set during the login
   * */
  @cask.get("/github/token")
  def ghToken(GtaGhAuthToken: cask.Cookie): Value = {
    try
      val token = GtaGhAuthToken.value
      ujson.Obj("token" -> token)
    catch
      case e: Exception =>
        println("Cookie not found")
        ujson.Obj("Error" -> "Token not found. Authenticate via /gh-login first.")
  }

  @cask.route("/github/token", methods = Seq("options"))
  def corsGhToken(request: cask.Request) = {"allow_cors"}

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
  def corsGithubRepos(request: cask.Request) = {"allow_cors"}

  @cask.get("/github/commits/:repo")
  def githubCommitsForRepo(request: cask.Request, repo: String): Value = {

    val token = request.headers("authorization").head
    val owner = "leo-pfeiffer"

    // todo: again, handle multiple pages
    // /repos/{owner}/{repo}/commits
    val url = s"$ghApiBaseUri/repos/${owner}/${repo}/commits?type=all&sort=full_name"
    val r = get(url, headers = Map("accept" -> "application/vnd.github.v3+json"))

    ujson.read(r.data.toString)

    // todo: convert to Gitlog and return

    // todo: problem, response does not include stats
    //  -> to get stats, need to call GET /repos/{owner}/{repo}/commits/{ref} on every commit
    //  -> will be VERY time consuming
    //  -> single api call to endpoint is ~ 50-300 ms... for 100 commits that's already 30s
  }

  @cask.route("/github/commits/:repo", methods = Seq("options"))
  def corsGithubCommitsForRepo(request: cask.Request, repo: String) = {"allow_cors"}

  initialize()

}
