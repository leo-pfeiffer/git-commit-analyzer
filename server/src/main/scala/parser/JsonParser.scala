package com.github.leopfeiffer.gitcommitanalyzer
package parser

import gitlog.{Commit, GitLog}

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_DATE_TIME
import scala.collection.mutable.ArrayBuffer

/**
 * Parse JSON based git logs as returned by the GitHub API.
 * */
object JsonParser {

  def main(log: ujson.Arr): GitLog = {
    val commits: ArrayBuffer[Commit] = new ArrayBuffer[Commit]()

    val obj: ArrayBuffer[ujson.Value] = log.arr

    commits.addAll(obj.map(x => {
      Commit(
        toStringTrim(x("sha")),
        toStringTrim(x("commit")("author")("name")),
        toStringTrim(x("commit")("author")("email")),
        LocalDateTime.parse(toStringTrim(x("commit")("author")("date")), ISO_DATE_TIME),
        toStringTrim(x("commit")("message")),
        "",
        Seq()
      )
    }))

    GitLog(commits)
  }

  def toStringTrim(x: ujson.Value): String = {
    x.toString.stripPrefix("\"").stripSuffix("\"")
  }

}
