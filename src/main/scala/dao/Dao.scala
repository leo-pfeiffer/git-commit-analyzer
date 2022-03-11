package com.github.leopfeiffer.gitcommitanalyzer
package dao

import filereader.FileReader

import cats.effect.*
import cats.implicits.*
import doobie.*
import doobie.implicits.*
import doobie.util.ExecutionContexts

// This is just for testing. Consider using cats.effect.IOApp instead of calling
// unsafe methods directly.
import cats.effect.unsafe.implicits.global

// Transactor that gets the database connection
val xa = Transactor.fromDriverManager[IO](
  "org.sqlite.JDBC", // driver
  "jdbc:sqlite:src/main/resources/dao/file.db", // connect URL
  "", // user
  "" // password
)

object Dao {

  def sqlString(s: String): Fragment = Fragment.const(s)

  def fileExecute(path: String): Unit = {
    val commands = FileReader.read(path, "")
      .replace("\n", s"")
      .split(";")

    for cmd <- commands
      do sqlString(cmd)
        .update
        .run
        .transact(xa)
        .unsafeRunSync()
  }

  def doSetup(): Unit = {
    fileExecute("src/main/resources/dao/drop.sql")
    fileExecute("src/main/resources/dao/schema.sql")
  }

  def insertUser(email: String): Unit = {
    sql"""INSERT INTO user (email) VALUES ($email)"""
      .update
      .run
      .transact(xa)
      .unsafeRunSync()
  }

  def deleteUser(userId: Int): Unit = {
    sql"""DELETE FROM user WHERE user_id=$userId"""
      .update
      .run
      .transact(xa)
      .unsafeRunSync()
  }

  // doSetup()
  
}