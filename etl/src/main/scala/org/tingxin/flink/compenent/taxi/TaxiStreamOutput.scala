package org.tingxin.flink.compenent.taxi

import org.apache.flink.api.common.serialization.{SimpleStringEncoder, SimpleStringSchema}
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.core.fs.FileSystem
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer
import org.apache.kafka.clients.CommonClientConfigs
import org.tingxin.flink.common.config.{CommonKafkaConfig, Parameters}
import org.tingxin.flink.common.struct.{BatchContext, FlinkOutput, StreamContext}
import org.tingxin.flink.common.util.KafkaUtils
import org.tingxin.flink.entity.taxi.TripRecord

import java.text.SimpleDateFormat
import java.util.Properties

trait TaxiStreamOutput extends FlinkOutput {
  this: StreamContext =>

  import TaxiParam._

  override def output(): Unit = {
    val folderPath =  getConfig.getString(s"$CONFIG_PREFIX.output.file.yellow.taxi")
    val output = getDataSet[DataStream[(String, Int, Double, Double, Int)]]("realtime_metric")
    val metricDs: DataStream[String] = output.map(item => {
      """{"load":%d, "distance":%f, "cost":%f, "count":%d, "time":"%s""".format(item._2, item._3, item._4, item._5, item._1)
    })
    // debug write csv
    metricDs.writeAsText(folderPath + "real-time-metric.csv",FileSystem.WriteMode.OVERWRITE)

    val outputKafkaParam = outputKafkaParamParameters()
    val kafkaSink = initOutPutKafka(outputKafkaParam)
    metricDs.addSink(kafkaSink).name("send-to-kafka")
  }


  def outputKafkaParamParameters(): Parameters = {
    KafkaUtils.getParams(BUSINESS_YELLOW_TAG, OUTPUT_KAFKA, getConfig)
  }

  def initOutPutKafka(kafkaParam: Parameters): FlinkKafkaProducer[String] = {
    val conf = CommonKafkaConfig(kafkaParam)
    val topic = kafkaParam.getString("kafka.topic")

    val properties = new Properties()
    properties.setProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, conf.kafkaBrokers)

    properties.setProperty("retries", "3")
    val producer = new FlinkKafkaProducer[String](topic, new SimpleStringSchema(), properties)
    producer.setWriteTimestampToKafka(true)
    producer
  }
}

