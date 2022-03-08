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
    matchPattern(rawCommit, raw"(?<=^Author:\s)[\S].*(?=\s<)".r)
  }

  def matchAuthorMail(rawCommit: String): String = {
    matchPattern(rawCommit, raw"(?<=^Author:\s.*<)[\S].*(?=>)".r)
  }

  def matchTimestamp(rawCommit: String): String = {
    matchPattern(rawCommit, raw"(?<=Date:\s+)[\d{4}][\S].*".r)
  }

  def matchHeader(rawCommit: String): String = {
    matchPattern(rawCommit, raw"(?<=\s{4})[\S].*".r)
  }

  def matchBody(rawCommit: String) = {
    // todo
  }

  def matchNodes(rawCommit: String): Iterator[String] = {
    // todo
    matchAll(rawCommit, raw"^([-\d]+)[\t\s]+([-\d]+)[\t\s]+.*".r)
  }

  def matchNodeAddition(rawNode: String): Int = {
    val num = matchGroup(rawNode, raw"^(\d+)([\s\t]+)([-\d]+)([\s\t]+)([\S].+)".r, 1)
    val hyphen = matchGroup(rawNode, raw"^(-)([\s\t]+)([-\d]+)([\s\t]+)([\S].+)".r, 1)
    if hyphen.equals("") then num.toInt else 0
  }

  def matchNodeDeletion(rawNode: String): Int = {
    val num = matchGroup(rawNode, raw"^([-\d]+)([\s\t]+)(\d+)([\s\t]+)([\S].+)".r, 3)
    val hyphen = matchGroup(rawNode, raw"^([-\d]+)([\s\t]+)(-)([\s\t]+)([\S].+)".r, 3)
    if hyphen.equals("") then num.toInt else 0
  }

  def matchNodeText(rawNode: String): String = {
    matchGroup(rawNode, raw"^[-\d]+[\s\t]+[-\d]+[\s\t]+([\S].+)".r, 1)
    }

  def makeNodeHelper(rawNode: String): Node = {
    val add: Int = matchNodeAddition(rawNode)
    val del: Int = matchNodeDeletion(rawNode)
    val txt: String = matchNodeText(rawNode)
    Node(add, del, txt)
  }

  def makeNodes(rawCommit: String): Seq[Node] = {
    matchNodes(rawCommit).map(makeNodeHelper).toSeq
  }

  def matchPattern(string: String, pattern: Regex): String = {
    pattern.findFirstIn(string).getOrElse("")
  }

  def matchAll(string: String, pattern: Regex): Regex.MatchIterator = {
    pattern.findAllIn(string)
  }

  def matchGroup(rawNode: String, pattern: Regex, group: Int): String = {
    if (pattern.matches(rawNode)) pattern.findAllIn(rawNode).group(group) else ""
  }

}
