# xi yun task
## Quick Start
1. 需要提前在部署环境按装好docker 环境,参考 [安装docker](https://docs.docker.com/engine/install/)
2. 进入script/kafka目录执行命令

    执行命令前请修改目录下的docker-compose.yml文件，替换文件中10.112.20.45 的地址为当前宿主机的本地IP ,请勿使用localhost,127.0.0.1
    ```shell
    docker-compose up -d
    ```

    如果安装的docker环境不自带 docker-compose,   请google查询并安装


3. 安装influxDb,模拟环境
   
    使用influxDb作为时序数据库，原因是其自带看板，并且支持单机版，Apache-doris不支持单机版，环境配置毕竟麻烦
    
    进入script/influDb目录，编辑telegraf.conf,需要替换文件中10.112.20.45 的地址为当前宿主机的本地IP
    ```shell
    docker run -d -p 8086:8086 \
      -v $PWD/influxdb2:/var/lib/influxdb2 \
      influxdb:latest
    ```
    打开localhost:8086 配置好账号,进入如下图(data => tokens),点击 tokens名称弹出token字符串，拷贝下来
   ![/script/resource/a.png](/script/resource/a.png)
   
      再次编辑 /script/influxdb/telegraf.conf,修改这些信息
   ```shell
     ## Token for authentication.
     token = "b5LxLOSNkpfzUD9Y1X4cbPi4Xa-eWGBSypd9yBiLKa-4dzVFHo9aWz2CTK1zfWpEMdKcBLwGNtLDHLYGNBQ9sg=="
   
     ## Organization is the name of the organization you wish to write to; must exist.
     organization = "org.verify.test"
   
     ## Destination bucket to write into.
     bucket = "event"
   ```
   
   再run

   ```shell
   docker run -v $PWD/telegraf.conf:/etc/telegraf/telegraf.conf:ro telegraf
   ```
   
4. 修改application.conf 文件，主要需要制定数据所在文件夹

目前没有模拟数据从网管发送到kafka,流程太麻烦，以直接读本地文件目录读形式，模拟数据输入

5，使用maven编译代码，注意，有两个app,都在src/main/scala/org/tingxin/flink/app 目录下
请修改maven分别编辑

TaxiApp是实时流APP，推送指标进入时序数据库，进行实时监控

TaxiStatApp是模拟离线分析，输出ETL 后的csv文件


   


