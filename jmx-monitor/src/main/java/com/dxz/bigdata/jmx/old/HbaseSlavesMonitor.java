//package com.dxz.bigdata.jmx.old;
//
//import com.alibaba.fastjson.JSONObject;
//import com.dxz.bigdata.jmx.utils.DateUtil;
//import com.dxz.bigdata.jmx.utils.InfluxDbUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//
//import static com.dxz.bigdata.jmx.constant.ItemConstants.*;
//import static com.dxz.bigdata.jmx.constant.ObjectConstants.*;
//import static com.dxz.bigdata.jmx.utils.JmxUtil.qryJSonObjectFromJMX;
//
///**
// * Desc: HBase RegionServer 关键指标监控
// * Created by yanlishuai on 2019-11-22
// */
//
//@Slf4j
//public class HbaseSlavesMonitor {
//
//    //创建一个CountDownLatch实例
//    private static volatile CountDownLatch countDownLatch = new CountDownLatch(22);
//
//    public static void main(String[] args) throws IOException, InterruptedException {
//        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//        cm.setMaxTotal(90);
//        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
//        Properties props = new Properties();
//        try {
//            props.load(HbaseSlavesMonitor.class.getResourceAsStream("/regionservers.properties"));
//        } catch (IOException e) {
//            log.error("获取配置文件失败: " + e.getMessage());
//        }
//        while(true) {
//            executeThreads(props, httpClient);
//            Thread.sleep(10000);
//        }
//    }
//    private static void executeThreads(Properties props, CloseableHttpClient httpClient) throws InterruptedException {
//        Long startTime = DateUtil.getTimestampMil();
//        ExecutorService executorService = Executors.newFixedThreadPool(props.size());
//        props.entrySet().forEach(entry -> {
//            executorService.submit(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        getMap(entry.getValue().toString(), startTime, httpClient);
//                    } catch (IOException e) {
//                        log.error("获取配置文件中 JMX URL 失败 {}", e.getCause());
//                    } finally {
//                        countDownLatch.countDown();
//                    }
//                    log.info(entry.getValue().toString() + "线程执行完毕！");
//                }
//            });
//        });
//        //等待所有子线程执行完毕，返回
//        countDownLatch.await();
//        Long endTime = DateUtil.getTimestampMil();
//        System.out.println("共耗时： " + String.valueOf(endTime - startTime) + " s");
//        executorService.shutdown();
//    }
//    private static void getMap(String HRegionServer, Long timestamp, CloseableHttpClient httpClient) throws IOException {
//        String ip = HRegionServer.split("/")[2].split(":")[0];
//        Map<String, Object> hbaseRegionServerMap = new HashMap<>();
//        JSONObject hbaseServerJson = qryJSonObjectFromJMX(httpClient, HRegionServer, HBASE_REGION_SERVER_SERVER);
//        JSONObject hbaseRegionJson = qryJSonObjectFromJMX(httpClient, HRegionServer, HBASE_REGION_SERVER_REGIONS);
//        JSONObject hbaseIpcJson = qryJSonObjectFromJMX(httpClient ,HRegionServer, HBASE_REGION_SERVER_IPC);
//        JSONObject hbaseJvmJson = qryJSonObjectFromJMX(httpClient ,HRegionServer, HBASE_JVM_METRICS);
//        hbaseRegionServerMap.put(REGION_COUNT, hbaseServerJson.getLong(REGION_COUNT));
//        hbaseRegionServerMap.put("SERVER_" + STORE_FILE_SIZE, hbaseServerJson.getLong(STORE_FILE_SIZE));
//        hbaseRegionServerMap.put("SERVER_" + STORE_FILE_COUNT, hbaseServerJson.getLong(STORE_FILE_COUNT));
//        hbaseRegionServerMap.put(HLOG_FILE_COUNT, hbaseServerJson.getLong(HLOG_FILE_COUNT));
//        hbaseRegionServerMap.put(TOTAL_REQUEST_COUNT, hbaseServerJson.getLong(TOTAL_REQUEST_COUNT));
//        hbaseRegionServerMap.put("SERVER_" + READ_REQUEST_COUNT, hbaseServerJson.getLong(READ_REQUEST_COUNT));
//        hbaseRegionServerMap.put("SERVER_" + WRITE_REQUEST_COUNT, hbaseServerJson.getLong(WRITE_REQUEST_COUNT));
//        hbaseRegionServerMap.put(NUM_OPEN_CONNECTIONS, hbaseIpcJson.getLong(NUM_OPEN_CONNECTIONS));
//        hbaseRegionServerMap.put(NUM_ACTIVE_HANDLER, hbaseIpcJson.getLong(NUM_ACTIVE_HANDLER));
//        hbaseRegionServerMap.put(FLUSH_QUEUE_LENGTH, hbaseServerJson.getLong(FLUSH_QUEUE_LENGTH));
//        hbaseRegionServerMap.put(COMPACTION_QUEUE_LENGTH, hbaseServerJson.getLong(COMPACTION_QUEUE_LENGTH));
//        hbaseRegionServerMap.put(GC_TIME_MILLIS, hbaseJvmJson.getLong(GC_TIME_MILLIS));
//        hbaseRegionServerMap.put(GC_TIME_MILLIS_PAR_NEW, hbaseJvmJson.getLong(GC_TIME_MILLIS_PAR_NEW));
//        hbaseRegionServerMap.put(GC_TIME_MILLIS_CONCURRENT_MARK_SWEEP, hbaseJvmJson.getLong(GC_TIME_MILLIS_CONCURRENT_MARK_SWEEP));
//        hbaseRegionServerMap.put(MEM_STORE_SIZE, hbaseServerJson.getLong(MEM_STORE_SIZE));
//        hbaseRegionServerMap.put(UPDATES_BLOCKED_TIME, hbaseServerJson.getLong(UPDATES_BLOCKED_TIME));
//        hbaseRegionServerMap.put(BLOCK_CACHE_HIT_COUNT, hbaseServerJson.getLong(BLOCK_CACHE_HIT_COUNT));
//        hbaseRegionServerMap.put(BLOCK_CACHE_MISS_COUNT, hbaseServerJson.getLong(BLOCK_CACHE_MISS_COUNT));
//        hbaseRegionServerMap.put(BLOCK_CACHE_EXPRESS_HIT_PERCENT, hbaseServerJson.getLong(BLOCK_CACHE_EXPRESS_HIT_PERCENT));
//        hbaseRegionServerMap.put(PERCENT_FILES_LOCAL, hbaseServerJson.getLong(PERCENT_FILES_LOCAL));
//        hbaseRegionServerMap.put(SLOW_GET_COUNT, hbaseServerJson.getLong(SLOW_GET_COUNT));
//        log.info("开始向 Influxdb 写入数据！！！");
//        InfluxDbUtil.insertInflux(hbaseRegionServerMap, timestamp, ip, "hbaseRegionServer");
//        log.info("向 influxd 插入数据完成");
//        System.out.println(timestamp + " : " + ip);
//        //hbaseRegionServerMap.entrySet().forEach(entry -> System.out.println(entry.getKey() + " : " + entry.getValue()));
//    }
//}
