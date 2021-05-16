package org.tingxin.flink.common.util

import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.tingxin.flink.common.config.{ConsumerConfig, Parameters, UnionConfig}
import org.tingxin.flink.common.entity.CanalFlatMessage
import org.tingxin.flink.common.ConsumerRecordDeserializationSchemaWrapper


object KafkaUtils {
  def getParams(key: String, kafkaPrefix: String, config: UnionConfig): Parameters = {
    val kafkaTopicParam = Parameters()
    kafkaTopicParam.set("kafka.brokers", config.getString(s"$kafkaPrefix.brokers"))
    kafkaTopicParam.set("kafka.username", config.getString(s"$kafkaPrefix.username"))
    kafkaTopicParam.set("kafka.password", config.getString(s"$kafkaPrefix.password"))
//    kafkaTopicParam.set("kafka.offset.auto.reset", config.getString(s"$kafkaPrefix.offset.auto.reset"))
    val topicName = config.getString(s"$kafkaPrefix.$key.topic.name")
    kafkaTopicParam.set("kafka.topic", topicName)
    kafkaTopicParam.set("kafka.consumer.groupId", config.getString(s"$kafkaPrefix.$key.consumer.groupId"))

    val offsetKey = s"$kafkaPrefix.$key.topic.offsets"
    val offset = if (config.has(offsetKey)) {
      config.getString(offsetKey)
    } else {
      ""
    }
    kafkaTopicParam.set("offsets", offset)

    kafkaTopicParam
  }

  def getOffset(kafkaParams: Parameters): Option[java.util.Map[KafkaTopicPartition, java.lang.Long]] = {
    val topicName = kafkaParams.getString("kafka.topic")
    // https://zhuanlan.zhihu.com/p/94592509
    val topicOffsets = kafkaParams.getString("offsets")
    if (topicOffsets!="") {
      val lst1 = topicOffsets.split(",")
      import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition
      val offsets: java.util.Map[KafkaTopicPartition, java.lang.Long] = new java.util.HashMap[KafkaTopicPartition, java.lang.Long]()
      for (e1 <- lst1) {
        val kv = e1.split(":")
        if (kv.length == 2) {
          val partition = kv(0).trim.toInt
          val offset = java.lang.Long.parseLong(kv(1).trim)
          offsets.put(new KafkaTopicPartition(topicName, partition), offset)
        }
      }
      if (!offsets.isEmpty) {
        return Some(offsets)
      }
    }
    None
  }

  def initCanalKafkaDs(env:StreamExecutionEnvironment, kafkaParam:Parameters): DataStream[CanalFlatMessage] = {

    val kafkaTopicConsumerConfig = ConsumerConfig(kafkaParam)
    val flinkKafkaConsumer =
      KafkaClientHelper.createKafkaConsumer[ConsumerRecord[Array[Byte], Array[Byte]]](kafkaTopicConsumerConfig, new ConsumerRecordDeserializationSchemaWrapper())

    val offsets = KafkaUtils.getOffset(kafkaParam)
    offsets match {
      case Some(offsets) => flinkKafkaConsumer.setStartFromSpecificOffsets(offsets)
      case _ => flinkKafkaConsumer.setStartFromGroupOffsets();
    }
    val inputDs = env.addSource(flinkKafkaConsumer)

      .map(x => {
        val rawJsonStr = new String(x.value, "utf-8")
        val canalFlatMessage: CanalFlatMessage = JsonUtils.fromJson[CanalFlatMessage](rawJsonStr)
        canalFlatMessage
      })
    inputDs
  }
}
