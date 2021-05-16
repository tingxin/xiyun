package org.tingxin.flink.compenent.taxi

import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.api.common.time.Time
import org.tingxin.flink.common.struct.BatchContext

import java.util.concurrent.TimeUnit

trait TaxiStatContext extends BatchContext {

  import TaxiParam._

  override def initEnv(): Unit = {
    super.initEnv()
    env.setRestartStrategy(
      RestartStrategies.fixedDelayRestart(
        5,
        Time.of(60, TimeUnit.SECONDS).toMilliseconds))
    //config checkpointing
    env.setParallelism(getConfig.getInt(s"$FLINK_PREFIX.parallelism"))
    env.getConfig.setLatencyTrackingInterval(30000) // latency sample interval 30s

  }
}

