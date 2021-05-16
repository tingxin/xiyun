package org.tingxin.flink.common.config


import org.tingxin.flink.common.util.Logging

import scala.collection.mutable

case class Parameters(param:  scala.collection.mutable.Map[String, Any]) extends Logging {

  def has(key: String): Boolean = {
    param.contains(key)
  }
  def getString(key: String): String = {
    if(param.contains(key)) {
      param(key).asInstanceOf[String]
    }else {
      logInfo(s"Failed to find $key in Parameters")
      throw new RuntimeException(s"Failed to find $key in Parameters")
    }
  }

  def getDouble(key: String): Double = {
    if(param.contains(key)) {
      param(key).asInstanceOf[Double]
    }else {
      logInfo(s"Failed to find $key in Parameters")
      throw new RuntimeException(s"Failed to find $key in Parameters")
    }
  }

  def getFloat(key: String): Float = {
    if(param.contains(key)) {
      param(key).asInstanceOf[Float]
    }else {
      logInfo(s"Failed to find $key in Parameters")
      throw new RuntimeException(s"Failed to find $key in Parameters")
    }
  }

  def getInt(key: String): Int = {
    if(param.contains(key)) {
      param(key).asInstanceOf[Int]
    }else {
      logInfo(s"Failed to find $key in Parameters")
      throw new RuntimeException(s"Failed to find $key in Parameters")
    }
  }

  def getLong(key: String): Long = {
    if(param.contains(key)) {
      param(key).asInstanceOf[Long]
    }else {
      logInfo(s"Failed to find $key in Parameters")
      throw new RuntimeException(s"Failed to find $key in Parameters")
    }
  }

  def getShort(key: String): Short = {
    if(param.contains(key)) {
      param(key).asInstanceOf[Short]
    }else {
      logInfo(s"Failed to find $key in Parameters")
      throw new RuntimeException(s"Failed to find $key in Parameters")
    }
  }

  def getBoolean(key: String): Boolean = {
    if(param.contains(key)) {
      param(key).asInstanceOf[Boolean]
    } else {
      logInfo(s"Failed to find $key in Parameters")
      throw new RuntimeException(s"Failed to find $key in Parameters")
    }
  }

  def set(key: String, value: Any): Unit = {
    param.put(key, value)
  }
}

object Parameters {
  def apply(): Parameters = {
    val param = mutable.Map[String, Any]()
    Parameters(param)
  }
}
