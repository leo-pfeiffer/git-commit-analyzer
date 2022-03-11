package com.github.leopfeiffer.gitcommitanalyzer
package dao

sealed abstract class QueryResult

case class Success(msg: String, data: Any) extends QueryResult
case class Fail(msg: String) extends QueryResult
