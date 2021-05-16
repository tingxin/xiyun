package org.tingxin.flink.entity.taxi

import java.text.SimpleDateFormat

case class TripRecord(
                       VendorID: Int,
                       tpep_pickup_datetime: String,
                       tpep_dropoff_datetime: String,
                       passenger_count: Int,
                       trip_distance: Double,
                       RatecodeID: Short,
                       store_and_fwd_flag: String,
                       PULocationID: Short,
                       DOLocationID: Short,
                       payment_type: Short,
                       fare_amount: Double,
                       extra: Double,
                       mta_tax: Double,
                       tip_amount: Double,
                       tolls_amount: Double,
                       improvement_surcharge: Double,
                       total_amount: Double,
                       congestion_surcharge: Double,
                       var speed: Double,
                       var duration: Double,
                       var valid: Boolean
                     )

object TripRecord {
  val df: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  def createEmpty():TripRecord = {
    TripRecord(0,"","",0,0,0,"",0,0,0,0,0,0,0,0,0,0,0,0,0, true)
  }
  def toJson(o: TripRecord): String = {
    val time = o.tpep_pickup_datetime
    val result =
      """{"time":"%s", "VendorID":%d, "tpep_pickup_datetime":"%s", "tpep_dropoff_datetime":"%s","passenger_count":%d, "trip_distance":%f, "RatecodeID":%d,"store_and_fwd_flag":"%s","PULocationID":%d,"DOLocationID":%d,"payment_type":%d,"fare_amount":%f,"extra":%f,"mta_tax":%f,"tip_amount":%f,"tolls_amount":%f,"improvement_surcharge":%f,"total_amount":%f,"congestion_surcharge":%f,"speed":%f}""".stripMargin.
        format(time, o.VendorID, o.tpep_pickup_datetime, o.tpep_dropoff_datetime, o.passenger_count, o.trip_distance,
          o.RatecodeID, o.store_and_fwd_flag, o.PULocationID, o.DOLocationID, o.payment_type, o.fare_amount, o.extra, o.mta_tax,
          o.tip_amount, o.tolls_amount, o.improvement_surcharge, o.total_amount, o.congestion_surcharge, o.speed)
    result
  }

  def toLocationTask(o: TripRecord): String = {
    val stamp = df.parse(o.tpep_dropoff_datetime).getTime * 1000000
    val result =
      """{"PULocationID":%d,"DOLocationID":%d,  "passenger_count":%d, "trip_distance":%f, "speed":%f,"duration":%f,"time":"%s"}""".stripMargin.
        format(o.PULocationID, o.DOLocationID, o.passenger_count, o.trip_distance, o.speed, o.duration, stamp)
    result
  }

  def toLocationTask3(o: TripRecord): String = {
    val stamp = df.parse(o.tpep_dropoff_datetime).getTime * 1000000
    val result =
      """{"name": "taxi","PULocationID":%d,"DOLocationID":%d,  "passenger_count":%d, "trip_distance":%f, "speed":%f,"duration":%f,"time":"%s"}""".stripMargin.
        format(o.PULocationID, o.DOLocationID, o.passenger_count, o.trip_distance, o.speed, o.duration, stamp)
    result
  }

  def toLocationTask2(o: TripRecord): String = {
    val stamp = df.parse(o.tpep_dropoff_datetime).getTime * 1000000
    val result =
      """{"stats":[{"tags":[{"field":"PULocationID","value":%d},{"field":"DOLocationID","value":%d}], "fields":{"passenger_count":%d, "trip_distance":%f, "speed":%f,"duration":%f},"time":"%s"}]}""".stripMargin.
        format(o.PULocationID, o.DOLocationID, o.passenger_count, o.trip_distance, o.speed, o.duration, stamp)
    result
  }
  def toCsv(o: TripRecord): String = {
    """VendorID=%d, tpep_pickup_datetime=%s, tpep_dropoff_datetime=%s, RatecodeID=%d,store_and_fwd_flag=%s,PULocationID=%d,DOLocationID=%d,payment_type=%d passenger_count=%d, trip_distance=%f,fare_amount=%f,extra=%f,mta_tax=%f,tip_amount=%f,tolls_amount=%f,improvement_surcharge=%f,total_amount=%f,congestion_surcharge=%f,speed=%f""".stripMargin.
      format(o.VendorID, o.tpep_pickup_datetime, o.tpep_dropoff_datetime, o.RatecodeID, o.store_and_fwd_flag,
        o.PULocationID, o.DOLocationID, o.payment_type, o.passenger_count, o.trip_distance, o.fare_amount, o.extra, o.mta_tax,
        o.tip_amount, o.tolls_amount, o.improvement_surcharge, o.total_amount, o.congestion_surcharge, o.speed)
  }
  def toInfluxCsv(o: TripRecord): String = {
    val stamp = df.parse(o.tpep_dropoff_datetime).getTime * 1000000
    val pickupStamp = df.parse(o.tpep_pickup_datetime).getTime * 1000000
    """taxi,VendorID=%d, tpep_pickup_datetime=%d, tpep_dropoff_datetime=%s, RatecodeID=%d,store_and_fwd_flag=%s,PULocationID=%d,DOLocationID=%d,payment_type=%d passenger_count=%d, trip_distance=%f,fare_amount=%f,extra=%f,mta_tax=%f,tip_amount=%f,tolls_amount=%f,improvement_surcharge=%f,total_amount=%f,congestion_surcharge=%f,speed=%f %d""".stripMargin.
      format(o.VendorID, pickupStamp, stamp, o.RatecodeID, o.store_and_fwd_flag,
        o.PULocationID, o.DOLocationID, o.payment_type, o.passenger_count, o.trip_distance, o.fare_amount, o.extra, o.mta_tax,
        o.tip_amount, o.tolls_amount, o.improvement_surcharge, o.total_amount, o.congestion_surcharge, o.speed, stamp)
  }
}