package org.tingxin.flink.common.util

import java.util.Properties
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema
import org.apache.flink.streaming.connectors.kafka._
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.{ConsumerConfig => KafkaConsumerConfig}
import org.apache.kafka.common.config.SaslConfigs
import org.tingxin.flink.common.config.{CommonKafkaConfig, ConsumerConfig}

object KafkaClientHelper {
  def createKafkaConsumer[T](consumerConfig: ConsumerConfig,
                             deserializationSchema: AbstractDeserializationSchema[T],
                             watermarkStrategy: WatermarkStrategy[T]
                            ): FlinkKafkaConsumerBase[T] = {
    val properties = new Properties()
    properties.setProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, consumerConfig.commonKafkaConfig.kafkaBrokers)
    properties.setProperty(KafkaConsumerConfig.GROUP_ID_CONFIG, consumerConfig.kafkaConsumerGroup)
    properties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT")
    properties.setProperty(SaslConfigs.SASL_MECHANISM, "PLAIN")
    properties.setProperty(KafkaConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerConfig.kafkaAutoOffsetReset)
    properties.setProperty(KafkaConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    properties.setProperty(SaslConfigs.SASL_JAAS_CONFIG,
      getJaasConfig(consumerConfig.commonKafkaConfig.kafkaUsername,
        consumerConfig.commonKafkaConfig.kafkaPassword))
    new FlinkKafkaConsumer[T](CommonKafkaConfig.TOPIC_CONFIG, deserializationSchema, properties)
      .assignTimestampsAndWatermarks(watermarkStrategy)
      .setStartFromGroupOffsets()
      .setCommitOffsetsOnCheckpoints(true)
  }

  def createKafkaConsumer[T](consumerConfig: ConsumerConfig,
                             deserializationSchema: AbstractDeserializationSchema[T]
                            ): FlinkKafkaConsumerBase[T] = {
    val properties = new Properties()
    properties.setProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, consumerConfig.commonKafkaConfig.kafkaBrokers)
    properties.setProperty(KafkaConsumerConfig.GROUP_ID_CONFIG, consumerConfig.kafkaConsumerGroup)
    properties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT")
    properties.setProperty(SaslConfigs.SASL_MECHANISM, "PLAIN")
    properties.setProperty(KafkaConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerConfig.kafkaAutoOffsetReset)
    properties.setProperty(KafkaConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    properties.setProperty(SaslConfigs.SASL_JAAS_CONFIG,
      getJaasConfig(consumerConfig.commonKafkaConfig.kafkaUsername,
        consumerConfig.commonKafkaConfig.kafkaPassword))
    new FlinkKafkaConsumer[T](consumerConfig.commonKafkaConfig.topic, deserializationSchema, properties)
      .setStartFromGroupOffsets()
      .setCommitOffsetsOnCheckpoints(true)
  }

  def createKafkaConsumer[T](consumerConfig: ConsumerConfig,
                             deserializationSchema: KafkaDeserializationSchema[T],
                             watermarkStrategy: WatermarkStrategy[T]
                            ): FlinkKafkaConsumerBase[T] = {
    val properties = new Properties()
    properties.setProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, consumerConfig.commonKafkaConfig.kafkaBrokers)
    properties.setProperty(KafkaConsumerConfig.GROUP_ID_CONFIG, consumerConfig.kafkaConsumerGroup)
    properties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT")
    properties.setProperty(SaslConfigs.SASL_MECHANISM, "PLAIN")
    properties.setProperty(KafkaConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerConfig.kafkaAutoOffsetReset)
    properties.setProperty(KafkaConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    properties.setProperty(SaslConfigs.SASL_JAAS_CONFIG,
      getJaasConfig(consumerConfig.commonKafkaConfig.kafkaUsername,
        consumerConfig.commonKafkaConfig.kafkaPassword))
    new FlinkKafkaConsumer[T](consumerConfig.commonKafkaConfig.topic, deserializationSchema, properties)
      .setStartFromGroupOffsets()
      .assignTimestampsAndWatermarks(watermarkStrategy)
      .setCommitOffsetsOnCheckpoints(true)
  }

  def createKafkaConsumer[T](consumerConfig: ConsumerConfig,
                             deserializationSchema: KafkaDeserializationSchema[T]
                            ): FlinkKafkaConsumerBase[T] = {
    val properties = new Properties()
    properties.setProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, consumerConfig.commonKafkaConfig.kafkaBrokers)
    properties.setProperty(KafkaConsumerConfig.GROUP_ID_CONFIG, consumerConfig.kafkaConsumerGroup)
    properties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT")
    properties.setProperty(SaslConfigs.SASL_MECHANISM, "PLAIN")
    properties.setProperty(KafkaConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerConfig.kafkaAutoOffsetReset)
    properties.setProperty(KafkaConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    properties.setProperty(SaslConfigs.SASL_JAAS_CONFIG,
      getJaasConfig(consumerConfig.commonKafkaConfig.kafkaUsername,
        consumerConfig.commonKafkaConfig.kafkaPassword))
    new FlinkKafkaConsumer[T](consumerConfig.commonKafkaConfig.topic, deserializationSchema, properties)
      .setStartFromGroupOffsets()
      .setCommitOffsetsOnCheckpoints(true)
  }

  def createKafkaProducer[T](commonKafkaConfig: CommonKafkaConfig,
                             serializationSchema: KafkaSerializationSchema[T]): FlinkKafkaProducer[T] = {
    val properties = new Properties()
    properties.setProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, commonKafkaConfig.kafkaBrokers)
    properties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT")
    properties.setProperty(SaslConfigs.SASL_MECHANISM, "PLAIN")
    properties.setProperty(KafkaConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    properties.setProperty(SaslConfigs.SASL_JAAS_CONFIG,
      getJaasConfig(commonKafkaConfig.kafkaUsername,
        commonKafkaConfig.kafkaPassword))
    new FlinkKafkaProducer[T](commonKafkaConfig.topic,serializationSchema,properties, FlinkKafkaProducer.Semantic.AT_LEAST_ONCE)
  }

  def getJaasConfig(username: String, password: String): String = {
    "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";".format(
      username,
      password)
  }
}
