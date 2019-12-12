package com.dxz.bigdata.jmx.test;

import com.dxz.bigdata.jmx.model.InfluxDBConnection;
import com.dxz.bigdata.jmx.model.ThreadMonitor;
import com.dxz.bigdata.jmx.monitor.DataNodesMonitor;
import com.dxz.bigdata.jmx.utils.MonitorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import static com.dxz.bigdata.jmx.utils.MonitorUtil.executeThreads;


/**
 * Desc: DataNode 指标监控主类
 * Created by yanlishuai 2019-12-10
 */
@Slf4j
public class DataNodesMonitorTest {
    //创建一个CountDownLatch实例
    private static volatile CountDownLatch countDownLatch ;

    public static void main(String[] args) throws IOException, InterruptedException {
        //初始化HTTP 连接池，可以同时为多个线程服务
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        //设置连接池的最大连接数
        cm.setMaxTotal(22);
        //使用CloseableHttpClient
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        //获取DataNode jmx 接口 url
        Properties props = MonitorUtil.getProperties("datanodes.properties");
        Properties influxDbProps = MonitorUtil.getProperties("influxdb.properties");
        InfluxDBConnection influxDBConnection = MonitorUtil.getInfluxDBConnection(influxDbProps);
        //实例化 DataNode 监控指标获取类的 对象
        ThreadMonitor monitor = new DataNodesMonitor();
        String table = "dataNodes";
        /**
         * 死循环，每10秒请求一次接口，获取监控指标
         */
        while (true) {
            countDownLatch = new CountDownLatch(22);
            MonitorUtil.insertIntoInfliuxDb(influxDBConnection, executeThreads(props, httpClient, monitor, countDownLatch), table);
            //主线程休眠10s
            Thread.sleep(10000);
        }
    }
}
