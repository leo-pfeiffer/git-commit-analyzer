package com.github.leopfeiffer.gitcommitanalyzer
package filereader

import scala.io.Source

object FileReader {

  def sayHello(name: String): Unit = {
    println("Hello, " + name + "!")
  }

  def read(path: String): String = {
    val bufferedSource = Source.fromFile(path)
    val stringBuilder = new StringBuilder()
    try {
      for (line <- bufferedSource.getLines) {
        stringBuilder.append(line)
        stringBuilder.append("\n")
      }
    } finally {
      bufferedSource.close
    }
    stringBuilder.toString()
  }
}
