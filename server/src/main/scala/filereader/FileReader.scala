package com.github.leopfeiffer.gitcommitanalyzer
package filereader

import scala.io.Source

object FileReader {

  def read(path: String): String = {
    read(path, "\n")
  }

  def read(path: String, delim: String): String = {
    val bufferedSource = Source.fromFile(path)
    val stringBuilder = new StringBuilder()
    try {
      for (line <- bufferedSource.getLines) {
        stringBuilder.append(line)
        stringBuilder.append(delim)
      }
    } finally {
      bufferedSource.close
    }
    stringBuilder.toString()
  }
}
