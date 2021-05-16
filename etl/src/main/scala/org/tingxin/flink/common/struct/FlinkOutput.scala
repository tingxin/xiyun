package org.tingxin.flink.common.struct

import org.tingxin.flink.common.util.Logging


trait FlinkOutput extends Logging {
  def output(): Unit = {
    logInfo("Start EventFlinkOutput ...")
  }
}
