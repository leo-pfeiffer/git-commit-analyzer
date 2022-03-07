package com.github.leopfeiffer.gitcommitanalyzer
package parser

import org.scalatest.funsuite.AnyFunSuite

class TxtParserTest extends AnyFunSuite {
  test("'matchNodeAddition' should get 0") {
    val result = TxtParser.matchNodeAddition("0\t0\ttest.txt")
    assert(result == 0)
  }

  test("'matchNodeAddition' should get single non-zero digit") {
    val result = TxtParser.matchNodeAddition("1\t0\ttest.txt")
    assert(result == 1)
  }

  test("'matchNodeAddition' should get double digit") {
    val result = TxtParser.matchNodeAddition("11\t0\ttest.txt")
    assert(result == 11)
  }

  test("'matchNodeAddition' should get triple digit") {
    val result = TxtParser.matchNodeAddition("111\t0\ttest.txt")
    assert(result == 111)
  }

  test("'matchHash' should get numbers") {
    val hash = "1234567890123456789012345678901234567890"
    val result = TxtParser.matchHash("notAHash " + hash)
    assert(result.equals(hash))
  }

  test("'matchHash' should get letters A-F") {
    val hash = "abcdefabcdabcdefabcdabcdefabcdabcdefabcd"
    val result = TxtParser.matchHash("notAHash " + hash)
    assert(result.equals(hash))
  }

  test("'matchHash' should get mix of letters A-F and numbers") {
    val hash = "abcdefabcdabcdefabcd12345678901234567890"
    val result = TxtParser.matchHash("notAHash " + hash)
    assert(result.equals(hash))
  }

  test("'matchHash' should not get letters other than A-F") {
    val hash = "asdfghjklmasdfghjklmasdfghjklmasdfghjklm"
    val result = TxtParser.matchHash("notAHash " + hash)
    assert(result.equals(""))
  }

  test("'matchHash' should not get space") {
    val hash = "abcdefabcdabcdefab dabcdefabcdabcdefabcd"
    val result = TxtParser.matchHash("notAHash " + hash)
    assert(result.equals(""))
  }

  test("'matchAuthorName' should get name") {
    val name = "some-name"
    val result = TxtParser.matchAuthorName("Author: " + name + " <foo")
    assert(result.equals(name))
  }

  test("'matchAuthorName' should not get name if 'Author' missing") {
    val name = "some-name"
    val result = TxtParser.matchAuthorName(name + " <foo")
    assert(result.equals(""))
  }

  test("'matchAuthorName' should not get name if '<' missing") {
    val name = "some-name"
    val result = TxtParser.matchAuthorName("Author: " + name + " foo")
    assert(result.equals(""))
  }

  test("'matchAuthorMail' should get mail") {
    val mail = "me@mail.com"
    val result = TxtParser.matchAuthorMail("Author: foo <" + mail + ">")
    assert(result.equals(mail))
  }

  test("'matchAuthorMail' should not get mail if '<' is missing") {
    val mail = "me@mail.com"
    val result = TxtParser.matchAuthorMail("Author: foo " + mail + ">")
    assert(result.equals(""))
  }

  test("'matchAuthorMail' should not get mail if '>' is missing") {
    val mail = "me@mail.com"
    val result = TxtParser.matchAuthorMail("Author: foo <" + mail + " foo")
    assert(result.equals(""))
  }
}
