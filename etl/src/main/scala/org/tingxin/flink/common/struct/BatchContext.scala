package org.tingxin.flink.common.struct

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.table.api.bridge.scala.BatchTableEnvironment
import org.apache.flink.table.api.{EnvironmentSettings, TableEnvironment}
import org.tingxin.flink.common.Constants
import org.tingxin.flink.common.config.UnionConfig
import org.tingxin.flink.common.enumeration.EnvType
import org.tingxin.flink.common.util.Logging

import scala.collection.mutable

trait BatchContext extends Logging {
  @transient protected var env: ExecutionEnvironment = _
  @transient protected var tEnv: BatchTableEnvironment = _
  @transient protected var config: UnionConfig = _

  protected val datasetContext: scala.collection.mutable.Map[String, Any] = scala.collection.mutable.Map[String, Any]()
  implicit protected val fc: BatchContext = this

  def init(args: Array[String]): Unit = {
    val params = ParameterTool.fromArgs(args)
    logInfo("Finished Params")
    config = UnionConfig(params)
    initEnv()
    logInfo("BatchContext finished init ...")
  }

  def initEnv(): Unit = {
    logInfo("BatchContext Init Env")
    env = ExecutionEnvironment.getExecutionEnvironment
  }

  def initTEnv(): Unit = {
    logInfo("BatchContext Init Table Env")
    tEnv = BatchTableEnvironment.create(env)
  }

  def run(name: String = "Batch App"): Unit = {
    val envType = if(config.has(Constants.ENV)) {
      EnvType.withName(config.getString(Constants.ENV).toUpperCase())
    } else {
      EnvType.withName("BATCH")
    }
    // TODO registerJobListener
    // env.registerJobListener()
    envType match {
      case EnvType.BATCH =>
        env.execute(name)
      case EnvType.TABLE =>
        tEnv.execute(name)
      case _ =>
        logInfo(s"Invalid Env Type $envType")
        throw new RuntimeException(s"Invalid Env Type $envType")
    }
    logInfo(s"Start to Run $name ...")
  }

  def getEnv: ExecutionEnvironment = env

  def setEnv(_env: ExecutionEnvironment): Unit = {
    env = _env
  }

  def getTEnv: TableEnvironment = tEnv

  def setTEnv(_tEnv: BatchTableEnvironment): Unit = {
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

  def setDataSet(key: String, value: Any): Unit = datasetContext += (key -> value)
}
