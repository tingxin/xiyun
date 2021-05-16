package org.tingxin.flink.common.struct

import org.tingxin.flink.common.util.Logging


trait FlinkInput extends Logging {
  def input(): Unit = {
    logInfo("Start EventFlinkInput...")
  }
}
