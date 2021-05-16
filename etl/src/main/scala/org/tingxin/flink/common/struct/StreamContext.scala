package org.tingxin.flink.common.struct


import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.tingxin.flink.common.Constants
import org.tingxin.flink.common.config.UnionConfig
import org.tingxin.flink.common.enumeration.EnvType
import org.tingxin.flink.common.util._

import scala.collection.mutable

trait StreamContext extends Logging {
  @transient protected var env: StreamExecutionEnvironment = _
  @transient protected var tEnv: StreamTableEnvironment = _
  @transient protected var config: UnionConfig = _

  protected val datasetContext: scala.collection.mutable.Map[String, Any] = scala.collection.mutable.Map[String, Any]()
  implicit protected val fc: StreamContext = this

  def init(args: Array[String]): Unit = {
    val params = ParameterTool.fromArgs(args)
    logInfo("Finished Params")
    config = UnionConfig(params)

    initEnv()
    initTEnv()
    StreamExecutionEnvironmentHelper.addGlobalConfiguration(env, params)
    logInfo("StreamContext finished init ...")
  }

  def initEnv(): Unit = {
    logInfo("StreamContext Init Env")
    env = StreamExecutionEnvironment.getExecutionEnvironment
  }

  def initTEnv(): Unit = {
    logInfo("StreamContext Init Table Env")
    val bsSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build()
    tEnv = StreamTableEnvironment.create(env, bsSettings)
  }

  def run(name: String = "Stream App"): Unit = {
    val envType = if(config.has(Constants.ENV)) {
      EnvType.withName(config.getString(Constants.ENV).toUpperCase())
    } else {
      EnvType.withName("STREAM")
    }
    // TODO registerJobListener
    // env.registerJobListener()
    envType match {
      case EnvType.STREAM =>
        env.execute(name)
      case EnvType.TABLE =>
        tEnv.execute(name)
      case _ =>
        logInfo(s"Invalid Env Type $envType")
        throw new RuntimeException(s"Invalid Env Type $envType")
    }
    logInfo(s"Start to Run $name ...")
  }

  def getEnv: StreamExecutionEnvironment = env

  def setEnv(_env: StreamExecutionEnvironment): Unit = {
    env = _env
  }

  def getTEnv: StreamTableEnvironment = tEnv

  def setTEnv(_tEnv: StreamTableEnvironment): Unit = {
    tEnv = _tEnv
  }

  def getConfig: UnionConfig = config

  def getDataSetContext: mutable.Map[String, Any] = datasetContext

  def getDataSet[T](name:String): T = fc.getDataSetContext.get(name) match {
    case Some(ds) => ds.asInstanceOf[T]
    case _ =>
      logError(s"Lack of $name in Component")
      throw new RuntimeException(s"Lack of $name in Component")
  }

  def setDataSet(key: String, value: Any):Unit = datasetContext += (key -> value)
}
