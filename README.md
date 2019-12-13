# HDFS YARN HBASE ZOOKEEPER 监控指标获取 并写入 influxdb 数据库

      这个Demo 主要是多线程 从 JMX 接口获取 HDFS、HBASE、ZOOKEEPER、YARN 各节点的常见监控指标的，然后将获取到的指标数据写入时序数据库 influxdb
      

     前端展示，可以采用 grafana