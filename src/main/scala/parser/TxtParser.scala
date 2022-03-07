package com.github.leopfeiffer.gitcommitanalyzer
package parser

 import gitlog.{Commit, GitLog, Node}

 import java.time.LocalDateTime
 import java.time.format.DateTimeFormatter.ISO_DATE_TIME
 import scala.collection.mutable.ArrayBuffer
 import scala.util.matching.Regex

object TxtParser {

  def main(log: String): GitLog = {
    val rawSplit = splitIntoCommits(log)

    val commits: ArrayBuffer[Commit] = new ArrayBuffer[Commit]()

    commits.addAll(rawSplit.map(x => {
      Commit(
        matchHash(x),
        matchAuthorName(x),
        matchAuthorMail(x),
        LocalDateTime.parse(matchTimestamp(x), ISO_DATE_TIME),
        matchHeader(x),
        makeNodes(x)
      )
    }))

    GitLog(commits)
  }

  def splitIntoCommits(log: String): Array[String] = {
    val commitStart = "(?=commit\\s[A-Fa-f0-9]{40})"
    log.split(commitStart)
  }

  def matchHash(rawCommit: String): String = {
    matchPattern(rawCommit, raw"([A-Fa-f0-9]{40})".r)
  }

  def matchAuthorName(rawCommit: String): String = {
    matchPattern(rawCommit, raw"(?<=Author:\s).*(?=\s<)".r)
  }

  def matchAuthorMail(rawCommit: String): String = {
    matchPattern(rawCommit, raw"(?<=Author:\s.*<).*(?=>)".r)
  }

  def matchTimestamp(rawCommit: String): String = {
    matchPattern(rawCommit, raw"(?<=Date:\s+)[\d{4}].*".r)
  }

  def matchHeader(rawCommit: String): String = {
    matchPattern(rawCommit, raw"(?<=\s{4})[A-Za-z].*".r)
  }

  def matchPattern(string: String, pattern: Regex): String = {
    pattern.findFirstIn(string).getOrElse("")
  }

  def matchNodes(rawCommit: String): Iterator[String] = {
    // todo maybe need to do (\d|-)..
    matchAll(rawCommit, raw"(\d+)\s+(\d+)\s+.*".r)
  }

  def matchNodeAddition(rawNode: String): Int = {
    matchPattern(rawNode, raw"^(?:\d+)+".r).toInt
  }

  def matchNodeDeletion(rawNode: String): Int = {
    matchPattern(rawNode, raw"(?<=\d+[\t\s+])(\d+)".r).toInt
  }

  def matchNodeText(rawNode: String): String = {
    matchPattern(rawNode, raw"(?<=\d+[\t\s+][\d+][\t\s+]).+".r)
    }

  def makeNodeHelper(rawNode: String): Node = {
    // todo handle delete file, where add / del is '-'
    val add: Int = matchNodeAddition(rawNode)
    val del: Int = matchNodeDeletion(rawNode)
    val txt: String = matchNodeText(rawNode)
    Node(add, del, txt)
  }

  def makeNodes(rawCommit: String): Seq[Node] = {
    matchNodes(rawCommit).map(makeNodeHelper).toSeq
  }

  def matchAll(string: String, pattern: Regex): Regex.MatchIterator = {
    pattern.findAllIn(string)
  }

}
