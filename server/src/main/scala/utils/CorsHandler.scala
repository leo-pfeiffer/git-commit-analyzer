package com.github.leopfeiffer.gitcommitanalyzer
package utils

import cask.internal.DispatchTrie
import cask.main.Routes
import cask.model.Response
import cask.router.{Decorator, EndpointMetadata, Result}
import io.undertow.server.HttpServerExchange
import cask.main.Main
import cask.util.Logger
import io.undertow.util.HttpString

import java.util
import scala.jdk.CollectionConverters.*

given log: Logger = new Logger.Console()

object CorsHandler:
  val accessControlAllowOrigin = new HttpString("Access-Control-Allow-Origin")
  val accessControlAllowCredentials = new HttpString("Access-Control-Allow-Credentials")
  val accessControlAllowHeaders = new HttpString("Access-Control-Allow-Headers")
  val accessControlAllowMethods = new HttpString("Access-Control-Allow-Methods")

  val origin = "*"
  val accepted = "true"
  val headers: util.Set[String] = Set("Authorization", "Content-Type", "X-Requested-With").asJava
  val methods: util.Set[String] = Set("POST", "GET", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS").asJava


/**
 * Cask CorsHandler.
 * Credits: https://github.com/objektwerks/cask/blob/master/src/main/scala/objektwerks/handler/CorsHandler.scala
 * */
class CorsHandler(dispatchTrie: DispatchTrie[Map[String, (Routes, EndpointMetadata[_])]],
                  mainDecorators: Seq[Decorator[_, _, _]],
                  debugMode: Boolean,
                  handleNotFound: () => Response.Raw,
                  handleMethodNotAllowed: () => Response.Raw,
                  handleError: (Routes, EndpointMetadata[_], Result.Error) => Response.Raw)
  extends Main.DefaultHandler (
    dispatchTrie,
    mainDecorators,
    debugMode,
    handleNotFound,
    handleMethodNotAllowed,
    handleError)(using log: Logger) {

  override def handleRequest(exchange: HttpServerExchange): Unit = {
    import CorsHandler.*
    exchange.getResponseHeaders
      .put(accessControlAllowOrigin, origin)
      .put(accessControlAllowCredentials, accepted)
      .putAll(accessControlAllowHeaders, headers)
      .putAll(accessControlAllowMethods, methods)
    super.handleRequest(exchange)

  }

}
