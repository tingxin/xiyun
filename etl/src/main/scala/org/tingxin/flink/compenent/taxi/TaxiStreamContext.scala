package org.tingxin.flink.compenent.taxi

import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup
import org.apache.flink.streaming.api.windowing.time.Time
import org.tingxin.flink.common.struct.StreamContext

import java.util.concurrent.TimeUnit

trait TaxiStreamContext extends StreamContext {

  import TaxiParam._

  override def initEnv(): Unit = {
    super.initEnv()
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setRestartStrategy(
      RestartStrategies.fixedDelayRestart(
        5,
        Time.of(60, TimeUnit.SECONDS).toMilliseconds))
    //config checkpointing
    env.setParallelism(getConfig.getInt(s"$FLINK_PREFIX.parallelism"))
    env.enableCheckpointing(1 * 1000)
//    env.setStateBackend(new RocksDBStateBackend(config.getString(s"$FLINK_PREFIX.$CHECKPOINT_PATH")))
    env.getCheckpointConfig.enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)
    env.getConfig.setLatencyTrackingInterval(30000) // latency sample interval 30s

    super.initTEnv()
  }
}
