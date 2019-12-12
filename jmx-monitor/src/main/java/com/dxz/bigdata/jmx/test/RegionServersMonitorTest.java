package com.dxz.bigdata.jmx.test;

import com.dxz.bigdata.jmx.model.InfluxDBConnection;
import com.dxz.bigdata.jmx.model.ThreadMonitor;
import com.dxz.bigdata.jmx.monitor.RegionServersMonitor;
import com.dxz.bigdata.jmx.utils.MonitorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import static com.dxz.bigdata.jmx.utils.MonitorUtil.executeThreads;

@Slf4j
public class RegionServersMonitorTest {
    //创建一个CountDownLatch实例
    private static volatile CountDownLatch countDownLatch ;

    public static void main(String[] args) throws IOException, InterruptedException {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        //设置连接池的最大连接数
        cm.setMaxTotal(22);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        Properties props = MonitorUtil.getProperties("regionservers.properties");
        Properties influxDbProps = MonitorUtil.getProperties("influxdb.properties");
        InfluxDBConnection influxDBConnection = MonitorUtil.getInfluxDBConnection(influxDbProps);
        ThreadMonitor monitor = new RegionServersMonitor();
        String table = "hbaseRegionServer";
        while (true) {
            countDownLatch = new CountDownLatch(22);
            MonitorUtil.insertIntoInfliuxDb(influxDBConnection, executeThreads(props, httpClient, monitor, countDownLatch), table);
            Thread.sleep(10000);
        }
    }
}
