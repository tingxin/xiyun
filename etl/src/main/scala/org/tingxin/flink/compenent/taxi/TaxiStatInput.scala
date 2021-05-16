package org.tingxin.flink.compenent.taxi
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.api.scala.DataSet
import org.tingxin.flink.common.struct.FlinkInput
import org.tingxin.flink.entity.taxi
import org.tingxin.flink.entity.taxi.TripRecord

import java.io.File

trait TaxiStatInput extends FlinkInput {
  this: TaxiStatContext =>

  import TaxiParam._


  override def input(): Unit = {
    val folderPath =  getConfig.getString(s"$CONFIG_PREFIX.input.file.yellow.taxi")
    val files = getFiles(folderPath)
    if (files.length == 0) {
      throw new IllegalArgumentException(s"$folderPath does not contain data cvs files")
    }
    var input: DataSet[String] = env.readTextFile(files(0))
    for (i <- 1 until files.length) {
      val newFileStream: DataSet[String] = env.readTextFile(files(i))
      input = input.union(newFileStream)
    }

    val ds:DataSet[taxi.TripRecord]= input.map(item=>{
      val fields = item.split(",")
      if (fields(0) != "VendorID"&&fields.length == 18) {
        // 1~8个字段不应该有空值
        val record = TripRecord(
          VendorID =if(fields(0)=="") -1 else fields(0).toInt,
          tpep_pickup_datetime = fields(1),
          tpep_dropoff_datetime = fields(2),
          passenger_count =if(fields(3)=="") -1 else fields(3).toShort,
          trip_distance =if(fields(4)=="") -1 else fields(4).toDouble,
          RatecodeID =if(fields(5)=="") -1 else fields(5).toShort,
          store_and_fwd_flag = fields(6),
          PULocationID =if(fields(7)=="") -1 else fields(7).toShort,
          DOLocationID =if(fields(8)=="") -1 else fields(8).toShort,
          payment_type =if(fields(9)=="") -1 else fields(9).toShort,
          fare_amount =if(fields(10)=="") -1 else fields(10).toDouble,
          extra =if(fields(11)=="") -1 else fields(11).toDouble,
          mta_tax =if(fields(12)=="") -1 else fields(12).toDouble,
          tip_amount =if(fields(13)=="") -1 else fields(13).toDouble,
          tolls_amount =if(fields(14)=="") -1 else fields(14).toDouble,
          improvement_surcharge =if(fields(15)=="") -1 else fields(15).toDouble,
          total_amount =if(fields(16)=="") -1 else fields(16).toDouble,
          congestion_surcharge =if(fields(17)=="") -1 else fields(17).toDouble,
          speed = 0,
          duration = 0,
          valid = true
        )
        record
      } else {
        TripRecord.createEmpty()
      }
    })

    setDataSet(TAXI_INPUT_DS, ds)
  }


  def getFiles(folderPath:String): Array[String] = {
    val file = new File(folderPath)
    file.listFiles().filter(!_.isDirectory).filter(t => t.toString.endsWith(".csv")).map(t => t.toString)
  }
}

