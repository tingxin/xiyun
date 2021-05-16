docker pull influxdb
sleep 2
docker run -d -p 8086:8086 \
      -v $PWD/influxdb2:/var/lib/influxdb2 \
      influxdb:latest

sleep 2
docker run -v $PWD/telegraf.conf:/etc/telegraf/telegraf.conf:ro telegraf