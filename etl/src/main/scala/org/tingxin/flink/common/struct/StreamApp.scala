package org.tingxin.flink.common.struct

import org.tingxin.flink.common.Constants


trait StreamApp {
  this: StreamContext
  with FlinkInput
  with FlinkCompute
  with FlinkOutput =>

  def initApp(args: Array[String]) = {
    logInfo("Start init app...")
    init(args)
  }

  def processApp(): Unit = {
    input()
    compute()
    output()
  }

  def execute() = {
    val name = if(getConfig.has(Constants.NAME)) {
      getConfig.getString(Constants.NAME)
    } else {
      "Flink App"
    }
    run(name)
  }


  def main(args: Array[String]): Unit = {
    initApp(args)
    processApp()
    execute()
  }

}
