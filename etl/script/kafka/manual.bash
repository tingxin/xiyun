docker-compose up -d

# docker-compose stop


#
#bin/kafka-topics.sh --create --topic test_data_event_mockup_code  --replication-factor 1 --partitions 1 --bootstrap-server 10.112.20.45:9092
#
#
#bin/kafka-topics.sh --create --topic test_stat_event  --replication-factor 1 --partitions 1 --bootstrap-server 10.10.128.142:9092,10.10.128.143:9092,10.10.128.145:9092
#
#
#bin/kafka-console-producer.sh --topic test_stat_event --bootstrap-server 10.110.3.15:9092
#
#bin/kafka-console-consumer.sh --topic test_stat_event --from-beginning --bootstrap-server 10.110.3.15:9092
#
#bin/kafka-console-producer.sh --topic test --bootstrap-server 10.110.3.15:9092
#
#bin/kafka-configs.sh --alter --add-config retention.hours=5 --topic test_data_event_sample_code --bootstrap-server=10.10.128.142:9092,10.10.128.143:9092,10.10.128.145:9092