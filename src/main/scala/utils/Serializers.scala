package com.github.leopfeiffer.gitcommitanalyzer
package utils

import org.json4s.{CustomSerializer, DefaultFormats, Formats, JString}

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_DATE_TIME

/**
 * Serializer for LocalDateTime objects
 * */
case object LDTSerializer extends CustomSerializer[LocalDateTime](
  format => ({
    case JString(s) => LocalDateTime.parse(s)
  }, {
    case ldt: LocalDateTime => JString(ldt.format(ISO_DATE_TIME))
  })
)

// add serializers here
// this needs to be important whenever the json4s read/write is used
implicit val formats: Formats = DefaultFormats + LDTSerializer
