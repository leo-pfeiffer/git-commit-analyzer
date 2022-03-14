package com.github.leopfeiffer.gitcommitanalyzer

import cask.main.Routes
import io.undertow.server.handlers.BlockingHandler

import utils.*
import routes.*

object Main extends cask.Main {

  // Use the cors handler instead
  override def defaultHandler: BlockingHandler = new BlockingHandler(
    new CorsHandler(dispatchTrie, mainDecorators, debugMode, handleNotFound, handleMethodNotAllowed, handleEndpointError)
  )

  override def port: Int = 9000

  val allRoutes = Seq(ApiRoutes(), GithubRoutes(), TestRoutes())
}
