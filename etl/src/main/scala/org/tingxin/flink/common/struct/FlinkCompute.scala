package org.tingxin.flink.common.struct

import org.tingxin.flink.common.util.Logging


trait FlinkCompute extends Logging {
  def compute(): Unit = {
    logInfo("Start EventFlinkCompute ...")
  }

}
