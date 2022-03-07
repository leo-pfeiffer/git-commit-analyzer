package com.github.leopfeiffer.gitcommitanalyzer
package parser

import org.scalatest.funsuite.AnyFunSuite;

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
}
