package org.tingxin.flink.common.entity

import java.util

import scala.collection.JavaConversions.mapAsJavaMap

case class CanalFlatMessage (
                         id: Option[Long] = None,
                         database: Option[String] = None,
                         table: Option[String] = None,
                         pkNames: Option[List[String]] = None,
                         isDdl: Option[Boolean] = None,
                         `type`: Option[String] = None,
                         es: Option[Long] = None,
                         ts: Option[Long] = None,
                         sqlType: Option[Map[String, Int]] = None,
                         mysqlType: Option[Map[String, String]] = None,
                         data: Option[List[Map[String, String]]] = None,
                         old: Option[List[Map[String, String]]] = None,
                         colNames: Option[List[String]] = None,
                         sql: Option[String] = None,
                         var kafkaTopic: Option[String] = None
                       ) {

  def getDataAsJava(): java.util.List[java.util.Map[String, String]] = {
    if(data.nonEmpty) {
      val out: java.util.List[java.util.Map[String, String]] = new util.ArrayList[util.Map[String, String]]()
      data.get.foreach(item => {
        out.add(mapAsJavaMap(item))
      })
      out
    } else {
      null
    }
  }
}

case class ParquetAvroCanalFlatMessage (
                                         id: Long = -1,
                                         database: String = "",
                                         table: String = "",
                                         //pkNames: List[String] = List(),
                                         isDdl: Boolean = false,
                                         `type`: String = "",
                                         es: Long = -1,
                                         ts: Long = -1,
                                         //sqlType: Map[String, Int] = Map(),
                                         //mysqlType: Map[String, String] = Map(),
                                         //data: List[Map[String, String]] = List(),
                                         //old: List[Map[String, String]] = List(),
                                         //colNames: List[String] = List(),
                                         sql: String = "",
                                         kafkaTopic: String = ""
                                       )