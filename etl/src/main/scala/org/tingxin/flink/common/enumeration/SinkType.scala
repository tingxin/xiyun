package org.tingxin.flink.common.enumeration

object SinkType extends Enumeration {
  type SinkType = Value
  val KAFKA, JDBC, ES, CASSANDRA, FS = Value

}
