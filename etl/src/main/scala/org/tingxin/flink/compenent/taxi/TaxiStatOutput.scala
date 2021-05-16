package org.tingxin.flink.compenent.taxi
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala.DataSet
import org.apache.flink.core.fs.FileSystem
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer
import org.apache.kafka.clients.CommonClientConfigs
import org.tingxin.flink.common.config.{CommonKafkaConfig, Parameters}
import org.tingxin.flink.common.struct.{BatchContext, FlinkOutput}
import org.tingxin.flink.common.util.KafkaUtils
import org.tingxin.flink.entity.taxi.TripRecord

import java.util.Properties

trait TaxiStatOutput extends FlinkOutput {
  this: BatchContext =>

  import TaxiParam._

  override def output(): Unit = {
    val folderPath =  getConfig.getString(s"$CONFIG_PREFIX.output.file.yellow.taxi")
    val noiseDs = getDataSet[DataSet[TripRecord]]("noise_metric")
    val noiseDsInfo = noiseDs.map(item=>TripRecord.toCsv(item))
    noiseDsInfo.writeAsText(folderPath + "noise.csv",FileSystem.WriteMode.OVERWRITE)

    val passengerDs = getDataSet[DataSet[(String,Int)]]("passengerDs")
    passengerDs.map(item=>"%s,%d".format(item._1,item._2)).writeAsText(folderPath + "passengerDs.csv",FileSystem.WriteMode.OVERWRITE)

    val distanceDs = getDataSet[DataSet[(String,Double)]]("distanceDs")
    distanceDs.map(item=>"%s,%f".format(item._1,item._2)).writeAsText(folderPath + "distanceDs.csv",FileSystem.WriteMode.OVERWRITE)

    val speedDs = getDataSet[DataSet[(String,Double)]]("speedDs")
    speedDs.map(item=>"%s,%f".format(item._1,item._2)).writeAsText(folderPath + "speedDs.csv",FileSystem.WriteMode.OVERWRITE)

    val taxiLoadStatDs = getDataSet[DataSet[(String,Short,Short, Int)]]("taxiLoadStatDs")
    taxiLoadStatDs.map(item=>"%s,%d, %d,%d".format(item._1,item._2, item._3,item._4)).writeAsText(folderPath + "taxiLoadStatDs.csv",FileSystem.WriteMode.OVERWRITE)

    val taxiFqDs = getDataSet[DataSet[(String,Short,Short, Int)]]("taxiFqDs")
    taxiFqDs.map(item=>"%s,%d, %d,%d".format(item._1,item._2, item._3,item._4)).writeAsText(folderPath + "taxiFqDs.csv",FileSystem.WriteMode.OVERWRITE)

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
