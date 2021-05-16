package org.tingxin.flink.app

import org.tingxin.flink.common.struct.StreamApp
import org.tingxin.flink.compenent.taxi._

object TaxiApp extends StreamApp
  with TaxiStreamContext
  with TaxiStreamInput
  with TaxiStreamCompute
  with TaxiStreamOutput
