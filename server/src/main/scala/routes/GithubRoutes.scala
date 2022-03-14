package com.github.leopfeiffer.gitcommitanalyzer
package routes

import requests.post
import ujson.Value.Value

class GithubRoutes()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes {

  val ghClientId: String = System.getenv("CLIENT_ID")
  val ghClientSecret: String = System.getenv("CLIENT_SECRET")
  val ghBaseUri = "https://github.com/login/oauth/access_token"
  val ghApiBaseUri = "https://api.github.com"
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

  @cask.get("/github/repos/:org")
  def githubRepos(org: String): Value = {

    // todo how to handle multiple pages?
    //  idea: repeat until all fetched: get page, append results
    val url = s"$ghApiBaseUri/orgs/${org}/repos?type=all&sort=full_name"
    val r = get(url, headers = Map("accept" -> "application/vnd.github.v3+json"))

    val obj: Value = ujson.read(r.data.toString)

    // todo map only names and return
  }

  @cask.get("/github/commits/:org/:repo")
  def githubCommitsForRepo(org: String, repo: String): Value = {

    // todo: again, handle multiple pages
    val url = s"$ghApiBaseUri/repos/${org}/${repo}/commits?type=all&sort=full_name"
    val r = get(url, headers = Map("accept" -> "application/vnd.github.v3+json"))

    val obj: Value = ujson.read(r.data.toString)

    // todo: convert to Gitlog and return

    // todo: problem, response does not include stats
    //  -> to get stats, need to call GET /repos/{owner}/{repo}/commits/{ref} on every commit
    //  -> will be VERY time consuming
    //  -> single api call to endpoint is ~ 50-300 ms... for 100 commits that's already 30s

  }

  initialize()

}
