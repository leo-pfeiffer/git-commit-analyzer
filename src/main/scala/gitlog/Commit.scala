package com.github.leopfeiffer.gitcommitanalyzer
package gitlog

import java.time.LocalDateTime

//commit 5e3458c0afd2fd57cff56ba45294b80334c51c98
//Author: leo-pfeiffer <leopold.pfeiffer@icloud.com>
//Date:   Fri Mar 4 20:36:46 2022 +0000
//
//  docs: added report
//
// -	-	doc/report.pdf
// 0	0	src/playSweeper.sh
case class Commit (
                    hash: String,
                    authorName: String,
                    authorMail: String,
                    timestamp: LocalDateTime,
                    header: String,
                    nodes: Seq[Node]
//                    body: String,
//                    commitType: CommitType,
//                    nodes: Array[Node]
                  )