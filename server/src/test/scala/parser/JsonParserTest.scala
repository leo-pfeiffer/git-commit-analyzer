package com.github.leopfeiffer.gitcommitanalyzer
package parser

import filereader.FileReader

import com.github.leopfeiffer.gitcommitanalyzer.gitlog.{Commit, GitLog}
import org.scalatest.funsuite.AnyFunSuite

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_DATE_TIME
import scala.collection.mutable.ArrayBuffer

class JsonParserTest extends AnyFunSuite {

  test("'blabla' bla bla") {
    val file = FileReader.read("src/test/resources/pagerank-git.json")
    val obj: ArrayBuffer[ujson.Value] = ujson.read(file).arr
    obj.foreach(x => {
      println(x("sha"))
    })
  }

  test("'blablablub' bla bla") {
    val file = FileReader.read("src/test/resources/pagerank-git.json")
    val obj = ujson.read(file).arr

    val r: GitLog = JsonParser.main(obj)

    println()
  }
}
