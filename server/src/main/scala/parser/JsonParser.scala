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

    // todo find way to elegantly strip trailing " from strings
    //  potentially, this could be done by typing the ArrayBuffer more accurately

    commits.addAll(obj.map(x => {
      Commit(
        x("sha").toString,
        x("commit")("author")("name").toString,
        x("commit")("author")("email").toString,
        LocalDateTime.parse(x("commit")("author")("date").toString.stripPrefix("\"").stripSuffix("\""), ISO_DATE_TIME),
        x("commit")("message").toString,
        "",
        Seq()
      )
    }))

    GitLog(commits)

  }

}
