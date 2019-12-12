package com.dxz.bigdata.jmx.test;

import com.dxz.bigdata.jmx.model.InfluxDBConnection;
import com.dxz.bigdata.jmx.model.ThreadMonitor;
import com.dxz.bigdata.jmx.monitor.MastersMonitor;
import com.dxz.bigdata.jmx.utils.MonitorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import static com.dxz.bigdata.jmx.utils.MonitorUtil.executeThreads;

@Slf4j
public class MastersMonitorTest {
    private static volatile CountDownLatch countDownLatch ;

    public static void main(String[] args) throws InterruptedException {
        //初始化HTTP 连接池，可以同时为多个线程服务
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        //设置连接池的最大连接数为15
        //cm.setMaxPerRoute();
        cm.setMaxTotal(3);
        //使用CloseableHttpClient
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        ThreadMonitor monitor = new MastersMonitor();
        //获取配置文件中的url
        Properties props = MonitorUtil.getProperties("master.properties");
        //加载 influxdb 配置文件
        Properties influxDbProps = MonitorUtil.getProperties("influxdb.properties");
        InfluxDBConnection influxDBConnection = MonitorUtil.getInfluxDBConnection(influxDbProps);
        String table = "masterServers";
//        while (true) {
        try {
            countDownLatch  = new CountDownLatch(3);
            MonitorUtil.insertIntoInfliuxDb(influxDBConnection, executeThreads(props, httpClient, monitor, countDownLatch), table);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
//
//            Thread.sleep(10000);
//        }
    }

}
