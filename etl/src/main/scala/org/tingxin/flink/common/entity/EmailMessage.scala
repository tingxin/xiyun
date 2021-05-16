package org.tingxin.flink.common.entity

case class Attachment (
  var fileName: Option[String] = None,
  var path: Option[String] = None,
  var content: Option[String] = None
                      )

case class EmailMessage  (
  var title: Option[String] = None,
  var `type`: Option[String] = None,
  var attachments: Option[List[Attachment]] = None,
  var content: Option[String] = None,
  var to: Option[String] = None,
  var cc: Option[String] = None,
  var bcc: Option[String] = None
                         )

case class OneMailConfig (
                           mail_to_list: Array[String],
                           exclude_table_set: Option[Set[String]] = None,
                           exclude_table_prefix_list: Option[List[String]] = None
                         )