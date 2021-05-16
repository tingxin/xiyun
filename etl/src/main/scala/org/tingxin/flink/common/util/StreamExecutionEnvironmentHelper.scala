package org.tingxin.flink.common.util

import java.util

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment


object StreamExecutionEnvironmentHelper {
  def isLocalEnvironment(env: StreamExecutionEnvironment): Boolean = {
    if (env.getJavaEnv.isInstanceOf[LocalStreamEnvironment]) {
      true
    } else {
      false
    }
  }

  def addGlobalConfiguration(env: StreamExecutionEnvironment, parameterTool: ParameterTool): Unit = {
    env.getConfig.setGlobalJobParameters(desensitize(parameterTool))
  }

  private def desensitize(parameterTool: ParameterTool): ParameterTool = {
    val result = new util.HashMap[String, String]()
    val properties = parameterTool.getProperties
    val iterator = properties.entrySet().iterator();
    while (iterator.hasNext) {
      val entry = iterator.next()
      val key = entry.getKey.toString
      val sensitive = key.contains("password") || key.contains("passwd")
      if (!sensitive) {
        result.put(key, entry.getValue.toString)
      }
    }
    ParameterTool.fromMap(result)
  }
}
