package com.github.leopfeiffer.gitcommitanalyzer
package parser

import filereader.FileReader

import org.scalatest.funsuite.AnyFunSuite

import java.time.{LocalDate, LocalDateTime, LocalTime}

class TxtParserTest extends AnyFunSuite {

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

  test("'matchHash' should work with branch info") {
    val hash = "30eb60e4d65c29b6459fc84c32613180a828cb79"
    val result = TxtParser.matchHash("commit " + hash + " (HEAD -> master)")
    assert(result.equals(hash))
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

  test("'matchNodeAddition' should get 0 with tab") {
    val result = TxtParser.matchNodeAddition("0\t0\ttest.txt")
    assert(result == 0)
  }

  test("'matchNodeAddition' should get single non-zero digit with tab") {
    val result = TxtParser.matchNodeAddition("1\t0\ttest.txt")
    assert(result == 1)
  }

  test("'matchNodeAddition' should get double digit with tab") {
    val result = TxtParser.matchNodeAddition("11\t0\ttest.txt")
    assert(result == 11)
  }

  test("'matchNodeAddition' should get triple digit with tab") {
    val result = TxtParser.matchNodeAddition("111\t0\ttest.txt")
    assert(result == 111)
  }

  test("'matchNOdeAddition' should get single digit") {
    val node = "1       2       nothing.sh"
    val result = TxtParser.matchNodeAddition(node)
    assert(result.equals(1))
  }

  test("'matchNOdeAddition' should get double digit") {
    val node = "12      2       nothing.sh"
    val result = TxtParser.matchNodeAddition(node)
    assert(result.equals(12))
  }

  test("'matchNOdeAddition' should get hyphen as 0") {
    val node = "-       2       nothing.sh"
    val result = TxtParser.matchNodeAddition(node)
    assert(result.equals(0))
  }

  test("'matchNodeDeletion' should get single digit") {
    val node = "1       2       nothing.sh"
    val result = TxtParser.matchNodeDeletion(node)
    assert(result.equals(2))
  }

  test("'matchNodeDeletion' should get double digit") {
    val node = "1      12       nothing.sh"
    val result = TxtParser.matchNodeDeletion(node)
    assert(result.equals(12))
  }

  test("'matchNodeDeletion' should get double digit after double digit") {
    val node = "13     12       nothing.sh"
    val result = TxtParser.matchNodeDeletion(node)
    assert(result.equals(12))
  }

  test("'matchNodeDeletion' should get hyphen as 0") {
    val node = "1       -       nothing.sh"
    val result = TxtParser.matchNodeDeletion(node)
    assert(result.equals(0))
  }

  test("'matchNodeDeletion' should get hyphen as 0 after hyphen") {
    val node = "-       -       nothing.sh"
    val result = TxtParser.matchNodeDeletion(node)
    assert(result.equals(0))
  }

  test("'matchNodeText' should get after two single digits") {
    val node = "1       2       nothing.sh"
    val result = TxtParser.matchNodeText(node)
    assert(result.equals("nothing.sh"))
  }

  test("'matchNodeText' should get after single digit, double digit") {
    val node = "1      12       nothing.sh"
    val result = TxtParser.matchNodeText(node)
    assert(result.equals("nothing.sh"))
  }

  test("'matchNodeText' should get after double digit, single digit") {
    val node = "12     1       nothing.sh"
    val result = TxtParser.matchNodeText(node)
    assert(result.equals("nothing.sh"))
  }

  test("'matchNodeText' should get after two double digits") {
    val node = "13     12       nothing.sh"
    val result = TxtParser.matchNodeText(node)
    assert(result.equals("nothing.sh"))
  }

  test("'matchNodeText' should get after single digit and hyphen") {
    val node = "1       -       nothing.sh"
    val result = TxtParser.matchNodeText(node)
    assert(result.equals("nothing.sh"))
  }

  test("'matchNodeText' should get after hyphen and single digit") {
    val node = "-       1       nothing.sh"
    val result = TxtParser.matchNodeText(node)
    assert(result.equals("nothing.sh"))
  }

  test("'matchNodeText' should get after double hyphen") {
    val node = "-       -       nothing.sh"
    val result = TxtParser.matchNodeText(node)
    assert(result.equals("nothing.sh"))
  }

  test("'matchTimeStamp' should get after Date:") {
    val date = "2022-03-08T08:11:29+00:00"
    val node = "foo bar\nAuthor: foo bar\nDate:   " + date + "\n\n"
    val result = TxtParser.matchTimestamp(node)
    assert(result.equals(date))
  }

  test("'matchTimeStamp' should not get without Date:") {
    val date = "2022-03-08T08:11:29+00:00"
    val node = "foo bar\nAuthor: foo bar\nFoo Bar:   " + date + "\n\n"
    val result = TxtParser.matchTimestamp(node)
    assert(result.equals(""))
  }

  test("'matchHeader' should read short header") {
    val longCommit = "commit 83d6daa0c8828e6730ddb0797a4518588b9b7a99\n" +
      "Author: leo-pfeiffer <leopold.pfeiffer@icloud.com>\n" +
      "Date:   2022-03-06T21:44:05+00:00\n\n" +
      "    test: this is a test\n    \n" +
      "    And this is the body of the commit message\n\n" +
      "1\t1\thello.txt"

    val result = TxtParser.matchHeader(longCommit)
    assert(result.equals("test: this is a test"))
  }


  test("'matchHeader' should read long header") {
    val longCommit = "commit 30eb60e4d65c29b6459fc84c32613180a828cb79 (HEAD -> master)\n" +
      "Author: leo-pfeiffer <leopold.pfeiffer@icloud.com>\n" +
      "Date:   2022-03-08T08:11:29+00:00\n\n    " +
      "This is a very very very long message Please scan " +
      "QR code for Test and Protect or ask a member of staff " +
      "for a form rector's cafe wholesome culture macbook pro nirvana\n\n" +
      "0       0       nkanslan.log\n"

    val result = TxtParser.matchHeader(longCommit)
    assert(result.length.equals(166))
  }

  test("'matchBody' should read one-part body") {
    val longCommit = "commit 92a435cf26bc71c8c31dca6c6fdfa63ca80dd4be (HEAD -> master)" +
      "\nAuthor: leo-pfeiffer <leopold.pfeiffer@icloud.com>\n" +
      "Date:   2022-03-08T10:45:17+00:00\n\n" +
      "    feat: an addition with a long commit body\n" +
      "    \n" +
      "    Lorem Ipsum is simply dummy text." +
      "\n\n" +
      "1       0       nkanslan.log"

    val expected = "Lorem Ipsum is simply dummy text.\n"

    val result = TxtParser.matchBody(longCommit)
    assert(result.equals(expected))
  }

  test("'matchBody' should read multi-part body") {
    val longCommit = "commit 92a435cf26bc71c8c31dca6c6fdfa63ca80dd4be (HEAD -> master)" +
      "\nAuthor: leo-pfeiffer <leopold.pfeiffer@icloud.com>\n" +
      "Date:   2022-03-08T10:45:17+00:00\n\n" +
      "    feat: a long body with two paragraphs\n    \n    " +
      "Foo bar 1.\n" +
      "    \n    " +
      "Foo bar 2." +
      "\n\n" +
      "1       0       nkanslan.log"

    val expected = "Foo bar 1.\nFoo bar 2.\n"

    val result = TxtParser.matchBody(longCommit)
    assert(result.equals(expected))
  }

  test("'matchNodes' should match single nodes") {
    val commit: String = "commit 5acf2d7aebcad417c5d73cc5dc41f66596786a98" +
      "\nAuthor: leo-pfeiffer <leopold.pfeiffer@icloud.com>" +
      "\nDate:   2022-03-07T07:24:29+00:00" +
      "\n\n    feat: new file" +
      "\n\n" +
      "2\t0\thello.txt"

    val result = TxtParser.matchNodes(commit).toSeq
    assert(result.length.equals(1))
  }

  test("'matchNodes' should match multiple nodes") {
    val commit: String = "commit 5acf2d7aebcad417c5d73cc5dc41f66596786a98" +
      "\nAuthor: leo-pfeiffer <leopold.pfeiffer@icloud.com>" +
      "\nDate:   2022-03-07T07:24:29+00:00" +
      "\n\n    feat: new file" +
      "\n\n" +
      "0\t0\tanotherfile.txt\n" +
      "2\t0\thello.txt"

    val result = TxtParser.matchNodes(commit).toSeq
    assert(result.length.equals(2))
  }

  test("'matchNodes' should match hyphen - num - node") {
    val commit: String = "commit 5acf2d7aebcad417c5d73cc5dc41f66596786a98" +
      "\nAuthor: leo-pfeiffer <leopold.pfeiffer@icloud.com>" +
      "\nDate:   2022-03-07T07:24:29+00:00" +
      "\n\n    feat: new file" +
      "\n\n" +
      "-\t0\thello.txt"

    val result = TxtParser.matchNodes(commit).toSeq
    assert(result.length.equals(1))
  }

  test("'matchNodes' should match num - hyphen - node") {
    val commit: String = "commit 5acf2d7aebcad417c5d73cc5dc41f66596786a98" +
      "\nAuthor: leo-pfeiffer <leopold.pfeiffer@icloud.com>" +
      "\nDate:   2022-03-07T07:24:29+00:00" +
      "\n\n    feat: new file" +
      "\n\n" +
      "1\t-\thello.txt"

    val result = TxtParser.matchNodes(commit).toSeq
    assert(result.length.equals(1))
  }

  test("'matchNodes' should match hyphen - hyphen - node") {
    val commit: String = "commit 5acf2d7aebcad417c5d73cc5dc41f66596786a98" +
      "\nAuthor: leo-pfeiffer <leopold.pfeiffer@icloud.com>" +
      "\nDate:   2022-03-07T07:24:29+00:00" +
      "\n\n    feat: new file" +
      "\n\n" +
      "-\t-\thello.txt"

    val result = TxtParser.matchNodes(commit).toSeq
    assert(result.length.equals(1))
  }

  test("'splitIntoCommits' should produce correct number of splits") {
    val content = FileReader.read("src/test/resources/test-gitlog.txt")
    val result = TxtParser.splitIntoCommits(content)
    assert(result.length.equals(13))
  }

  // todo more unit tests for splitIntoCommits
}
