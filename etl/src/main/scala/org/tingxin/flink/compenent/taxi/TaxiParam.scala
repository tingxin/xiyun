package org.tingxin.flink.compenent.taxi

object TaxiParam {

  val CONFIG_PREFIX = "xiyun.taxi.app"
  val OUTPUT_KAFKA = s"$CONFIG_PREFIX.output.kafka"
  val APPLICATION_PREFIX = s"$CONFIG_PREFIX.application"
  val FLINK_PREFIX = s"$CONFIG_PREFIX.flink"
  val KAFKA_PREFIX = s"$CONFIG_PREFIX.kafka"
  val DORIS_KAFKA_PREFIX = s"$CONFIG_PREFIX.doris_kafka"
  val REALTIME_KAFKA_PREFIX = s"$CONFIG_PREFIX.realtime_kafka"
  val MYSQL_PREFIX = s"$CONFIG_PREFIX.mysql"
  val BUSINESS_YELLOW_TAG = "yellow"
  val CHECKPOINT_PATH = "checkpoint.path"
  val TAXI_INPUT_DS = "taxi_input_dataset"
  val TAXI_INVALID_INPUT_DS = "taxi_invalid_input_dataset"
}
