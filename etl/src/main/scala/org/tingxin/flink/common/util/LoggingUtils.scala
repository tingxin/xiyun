package org.tingxin.flink.common.util

import java.io.{PrintWriter, StringWriter}

import scala.util.matching.Regex

/**
  * Created by huaidong.tang on 07/07/2017.
  */
object LoggingUtils extends Logging {

  def getException(ex: Throwable) = {
    val sw = new StringWriter
    ex.printStackTrace(new PrintWriter(sw))
    sw.toString
  }

  def loggingInfo(msg: => String): Unit = super.logInfo(msg)

  def loggingDebug(msg: => String): Unit = super.logDebug(msg)

  def loggingTrace(msg: => String): Unit = super.logTrace(msg)

  def loggingWarning(msg: => String): Unit = super.logWarning(msg)

  def loggingError(msg: => String): Unit = super.logError(msg)

  def loggingInfo(msg: => String, throwable: Throwable): Unit = super.logInfo(msg, throwable)

  def loggingDebug(msg: => String, throwable: Throwable): Unit = super.logDebug(msg, throwable)

  def loggingTrace(msg: => String, throwable: Throwable): Unit = super.logTrace(msg, throwable)

  def loggingWarning(msg: => String, throwable: Throwable): Unit = super.logWarning(msg, throwable)

  def loggingError(msg: => String, throwable: Throwable): Unit = super.logError(msg, throwable)

  def main(args: Array[String]): Unit = {
    val inputRecord = "20170420_000003,其他,https://attachment.evchargeonline.com/group1/M00/00/00/CuBmU1hjD5qAYd9HAAHMCMUxF_k420.jpg,074782068,EvCard分时租赁,7076,虹口区政府,1.5 元/度,0.05km,0,0,5,3,专用车位,0 元/度,121.5056,31.2646,121.512195,31.270224,2,上海市虹口区飞虹路518号（近大连路）,0"

    val fields = inputRecord.split(",")
    val pattern = new Regex("\\d+\\.?\\d*")
    println(fields(7))
    println(pattern.findFirstIn(fields(7)).getOrElse(0))
  }
}
