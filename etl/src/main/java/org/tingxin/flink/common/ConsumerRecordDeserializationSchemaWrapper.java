package org.tingxin.flink.common;


import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.GenericTypeInfo;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class ConsumerRecordDeserializationSchemaWrapper implements KafkaDeserializationSchema<ConsumerRecord<byte[], byte[]>> {

    private static final long serialVersionUID = 3651665280744549932L;

    //private final DeserializationSchema deserializationSchema;

    public ConsumerRecordDeserializationSchemaWrapper(/*DeserializationSchema<ConsumerRecord<byte[], byte[]>> deserializationSchema*/) {
        //this.deserializationSchema = deserializationSchema;
    }

    @Override
    public ConsumerRecord<byte[], byte[]> deserialize(ConsumerRecord<byte[], byte[]> record) throws Exception {
        return record;
    }

    @Override
    public boolean isEndOfStream(ConsumerRecord<byte[], byte[]> nextElement) {
        return false;
    }

    @Override
    public TypeInformation<ConsumerRecord<byte[], byte[]>> getProducedType() {
        ConsumerRecord<byte[], byte[]> c = new ConsumerRecord<byte[], byte[]>("", -1, -1, null, null);

        return new GenericTypeInfo<ConsumerRecord<byte[], byte[]>>((Class<ConsumerRecord<byte[], byte[]>>)c.getClass());

    }
}