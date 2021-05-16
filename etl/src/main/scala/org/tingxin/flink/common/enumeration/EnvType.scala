package org.tingxin.flink.common.enumeration

object EnvType extends Enumeration {
  type EnvType = Value
  val STREAM,BATCH, TABLE = Value
}
