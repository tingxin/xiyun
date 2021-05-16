package org.tingxin.flink.compenent.taxi

import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala.{DataSet, createTypeInformation}
import org.tingxin.flink.common.struct.FlinkCompute
import org.tingxin.flink.entity.taxi.TripRecord

import java.text.SimpleDateFormat

trait TaxiStatCompute extends FlinkCompute {
  this: TaxiStatContext =>

  import TaxiParam._


  override def compute(): Unit = {
    val input = getDataSet[DataSet[TripRecord]](TAXI_INPUT_DS)
    val df: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val dfDay: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val dfYear: SimpleDateFormat = new SimpleDateFormat("yyyy")
    val invalidDs = input.filter(item => item.valid == false)
    setDataSet("invalid_metric", invalidDs)

    val validDs = input.filter(item => item.valid)
    val noise1 = validDs.filter(item => item.tpep_dropoff_datetime == "" || item.tpep_pickup_datetime == "")


    val inputEx = validDs
      .filter(item => item.tpep_dropoff_datetime != "" && item.tpep_pickup_datetime != "")
      .map(item => {
        val begin = df.parse(item.tpep_pickup_datetime)
        val end = df.parse(item.tpep_dropoff_datetime)
        val between = end.getTime - begin.getTime
        val hour = between / 1000 / 3600.0
        item.duration = hour
        item
      })

    val noise2 = inputEx.filter(item => item.trip_distance <= 0 || item.duration <= 0 || item.passenger_count <= 0)
    val noiseDs = noise1.union(noise2)
    setDataSet("noise_metric", noiseDs)

    val basicDs = inputEx.filter(item => item.trip_distance > 0 && item.duration > 0 && item.passenger_count > 0)
      .map(item => {
        item.speed = item.trip_distance / item.duration
        item
      })

    trendAnalysis(basicDs, df, dfDay)
    hotLocationAnalysis(basicDs, df, dfYear)
  }

  def trendAnalysis(basicDs: DataSet[TripRecord], df: SimpleDateFormat, aggDf: SimpleDateFormat): Unit = {
    // 趋势分析按天(载人数，行驶距离，行驶速度)
    val dailyMetric = basicDs.map(item => {
      val a = df.parse(item.tpep_dropoff_datetime)
      val dt = aggDf.format(a)
      (dt, item.passenger_count, item.trip_distance, item.speed)
    }).sortPartition(0, Order.ASCENDING)

    val passengerDs = dailyMetric.map(item => (item._1, item._2)).groupBy(0).sum(1)
    setDataSet("passengerDs", passengerDs)

    val distanceDs = dailyMetric.map(item => (item._1, item._3)).groupBy(0).sum(1)
    setDataSet("distanceDs", distanceDs)

    val speedDs = dailyMetric.map(item => (item._1, item._4)).groupBy(0).sum(1)
    setDataSet("speedDs", speedDs)
  }

  def hotLocationAnalysisWithHour(basicDs: DataSet[TripRecord], df: SimpleDateFormat, aggDf: SimpleDateFormat): Unit = {
    val g = basicDs.map(item => {
      val a = df.parse(item.tpep_dropoff_datetime)
      val dt = aggDf.format(a)
      val hour = a.getHours()
      (dt, item.PULocationID, item.DOLocationID, hour, item.passenger_count, 1)
    }).sortPartition(0, Order.ASCENDING)

    val taxiLoadStatDs = g.map(item => (item._1, item._2, item._3, item._4, item._5)).groupBy(0, 1, 2, 3).sum(4)
    setDataSet("taxiLoadStatDs", taxiLoadStatDs)

    val taxiFqDs = g.map(item => (item._1, item._2, item._3, item._4, item._6)).groupBy(0, 1, 2, 3).sum(4)
    setDataSet("taxiFqDs", taxiFqDs)

  }

  def hotLocationAnalysis(basicDs: DataSet[TripRecord], df: SimpleDateFormat, aggDf: SimpleDateFormat): Unit = {
    val g = basicDs.map(item => {
      val a = df.parse(item.tpep_dropoff_datetime)
      val dt = aggDf.format(a)
      (dt, item.PULocationID, item.DOLocationID, item.passenger_count, 1)
    }).sortPartition(0, Order.ASCENDING)

    val taxiLoadStatDs = g.map(item => (item._1, item._2, item._3, item._4)).groupBy(0, 1, 2).sum(3)
    setDataSet("taxiLoadStatDs", taxiLoadStatDs)

    val taxiFqDs = g.map(item => (item._1, item._2, item._3, item._5)).groupBy(0, 1, 2).sum(3)
    setDataSet("taxiFqDs", taxiFqDs)

  }
}

