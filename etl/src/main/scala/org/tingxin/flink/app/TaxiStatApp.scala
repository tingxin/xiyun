package org.tingxin.flink.app

import org.tingxin.flink.common.struct.BatchApp
import org.tingxin.flink.compenent.taxi._

object TaxiStatApp extends BatchApp
  with TaxiStatContext
  with TaxiStatInput
  with TaxiStatCompute
  with TaxiStatOutput
