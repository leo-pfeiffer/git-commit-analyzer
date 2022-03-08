package com.github.leopfeiffer.gitcommitanalyzer
package gitlog

import java.time.LocalDateTime

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