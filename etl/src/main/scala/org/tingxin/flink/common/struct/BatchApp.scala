package org.tingxin.flink.common.struct

import org.tingxin.flink.common.Constants

trait BatchApp {
  this: BatchContext
    with FlinkInput
    with FlinkCompute
    with FlinkOutput =>

  def initApp(args: Array[String]): Unit = {
    logInfo("Start init app...")
    init(args)
  }

  def processApp(): Unit = {
    input()
    compute()
    output()
  }

  def execute(): Unit = {
    val name = if(getConfig.has(Constants.NAME)) {
      getConfig.getString(Constants.NAME)
    } else {
      "Batch App"
    }
    run(name)
  }


  def main(args: Array[String]):Unit = {
    initApp(args)
    processApp()
    execute()
    logInfo("task done")
  }

}
