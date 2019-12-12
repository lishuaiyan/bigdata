package com.dxz.bigdata.jmx.test;

import com.dxz.bigdata.jmx.model.InfluxDBConnection;
import com.dxz.bigdata.jmx.model.ThreadMonitor;
import com.dxz.bigdata.jmx.monitor.ZkServersMonitor;
import com.dxz.bigdata.jmx.utils.MonitorUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import static com.dxz.bigdata.jmx.utils.MonitorUtil.executeThreads;

@Slf4j
public class ZkServersMonitorTest {
    private static volatile CountDownLatch countDownLatch;

    public static void main(String[] args) throws IOException, InterruptedException {
        ThreadMonitor monitor = new ZkServersMonitor();
        Properties props = MonitorUtil.getProperties("zookeeper.properties");
        Properties influxDbProps = MonitorUtil.getProperties("influxdb.properties");
        InfluxDBConnection influxDBConnection = MonitorUtil.getInfluxDBConnection(influxDbProps);
        String table = "zkServers";

        while (true) {
            log.info("开始获取监控指标");
            countDownLatch = new CountDownLatch(3);
            MonitorUtil.insertIntoInfliuxDb(influxDBConnection, executeThreads(props, null, monitor, countDownLatch), table);
            log.info("指标数据写入influxdb完成");
            Thread.sleep(10000);
        }
    }
}
