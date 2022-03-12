package com.github.leopfeiffer.gitcommitanalyzer
package gitlog

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_DATE_TIME
import upickle.default._

case class Commit (
                    hash: String,
                    authorName: String,
                    authorMail: String,
                    timestamp: LocalDateTime,
                    header: String,
                    body: String,
                    nodes: Seq[Node],
//                    commitType: CommitType,
                  )