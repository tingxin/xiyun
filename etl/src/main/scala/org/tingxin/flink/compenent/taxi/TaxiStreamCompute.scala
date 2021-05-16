package org.tingxin.flink.compenent.taxi

import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.functions.{KeyedProcessFunction, ProcessFunction}
import org.apache.flink.api.scala.{DataSet, createTypeInformation}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.api.windowing.assigners.{TumblingEventTimeWindows, TumblingProcessingTimeWindows}
import org.tingxin.flink.common.struct.FlinkCompute
import org.tingxin.flink.entity.taxi.TripRecord
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.table.api.FieldExpression
import org.apache.flink.util.Collector

import java.text.SimpleDateFormat

trait TaxiStreamCompute extends FlinkCompute {
  this: TaxiStreamContext =>

  import TaxiParam._


  override def compute(): Unit = {
    val df: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val dfDay: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val input = getDataSet[DataStream[TripRecord]](TAXI_INPUT_DS)
    val validDs = input.filter(item => item.valid)
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


    val basicDs = inputEx.filter(item => item.trip_distance > 0 && item.duration > 0 && item.passenger_count > 0)
      .map(item => {
        item.speed = item.trip_distance / item.duration
        item
      })

    val ds = taxiRealTimeIndicator(basicDs, df, dfDay)
    setDataSet("realtime_metric", ds)
  }

  def taxiRealTimeIndicator(ds: DataStream[TripRecord], df: SimpleDateFormat, aggDf: SimpleDateFormat): DataStream[(String, Int, Double, Double, Int)] = {
    // 当天实时车载数量,行车距离，车费，车次
    ds.map(item => {
      (item.tpep_dropoff_datetime, item.passenger_count, item.trip_distance, item.total_amount, 1)
    }).keyBy(item => {
      // 同一天的数据分区
      val end = df.parse(item._1)
      aggDf.format(end)
    }).reduce((item1, item2) => {
      (item1._1, item2._2 + item1._2, item1._3 + item2._3, item1._4 + item2._4, item1._5 + item2._5)
    })
  }


}

