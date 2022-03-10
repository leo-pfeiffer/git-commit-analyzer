package com.github.leopfeiffer.gitcommitanalyzer
package utils

import gitlog.{Commit, CommitType, GitLog, Node}

import upickle.default._
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_DATE_TIME


implicit val localDateTimeRW: ReadWriter[LocalDateTime] =
  readwriter[String].bimap[LocalDateTime](
    ldt => ldt.format(ISO_DATE_TIME),
    str => LocalDateTime.parse(str),
  )

implicit val nodeRW: ReadWriter[Node] = upickle.default.macroRW[Node]

implicit val commitRW: ReadWriter[Commit] = upickle.default.macroRW[Commit]

implicit val commitTypeRW: ReadWriter[CommitType] = upickle.default.macroRW[CommitType]

implicit val gitlogRW: ReadWriter[GitLog] = upickle.default.macroRW[GitLog]

