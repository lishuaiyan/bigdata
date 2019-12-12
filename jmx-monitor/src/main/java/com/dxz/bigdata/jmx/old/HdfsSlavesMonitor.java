//package com.dxz.bigdata.jmx.old;
//
//import com.alibaba.fastjson.JSONObject;
//import com.dxz.bigdata.jmx.utils.DateUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import static com.dxz.bigdata.jmx.constant.ItemConstants.*;
//import static com.dxz.bigdata.jmx.constant.ObjectConstants.HADOOP_DATA_NODE_JVM_METRICS;
//import static com.dxz.bigdata.jmx.constant.ObjectConstants.HADOOP_DATA_NODE_RPC_ACTIVITY_FOR_PORT_50020;
//import static com.dxz.bigdata.jmx.utils.JmxUtil.qryJSonObjectFromJMX;
//@Slf4j
//public class HdfsSlavesMonitor {
//    //创建一个CountDownLatch实例
//    private static volatile CountDownLatch countDownLatch = new CountDownLatch(22);
//    public static void main(String[] args) throws IOException, InterruptedException {
//        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//        cm.setMaxTotal(65);
//        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
//        Properties props = new Properties();
//        try {
//            props.load(HdfsSlavesMonitor.class.getResourceAsStream("/datanodes.properties"));
//        } catch (IOException e) {
//            log.error("获取配置文件失败: " + e.getMessage());
//        }
//        while(true) {
//            executeThreads(httpClient, props);
//            Thread.sleep(10000);
//        }
//    }
//    private static void executeThreads(CloseableHttpClient httpClient, Properties props) throws InterruptedException {
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
//
//        });
//        //等待所有子线程执行完毕，返回
//        countDownLatch.await();
//        Long endTime = DateUtil.getTimestampMil();
//        System.out.println("共耗时： " + String.valueOf(endTime - startTime) + " s");
//        executorService.shutdown();
//    }
//    private static void getMap(String dataNode, Long timestamp, CloseableHttpClient httpClient) throws IOException {
//        String ip = dataNode.split("/")[2].split(":")[0];
//        Map<String,Double> hdfsDataNodeMap = new HashMap<>();
//        //"Hadoop:service=DataNode,name=DataNodeActivity-cu-9e-023-hd0-91-100-50010"
//        JSONObject hdfsDataNodeJson = qryJSonObjectFromJMX(httpClient, dataNode, "Hadoop:service=DataNode,name=DataNodeActivity-" + ip + "-50010");
//        JSONObject hdfsDataNodeRpcJson = qryJSonObjectFromJMX(httpClient, dataNode, HADOOP_DATA_NODE_RPC_ACTIVITY_FOR_PORT_50020);
//        JSONObject hdfsDataNodeJvmJson = qryJSonObjectFromJMX(httpClient, dataNode, HADOOP_DATA_NODE_JVM_METRICS);
//
//        hdfsDataNodeMap.put(READ_BLOCK_OP_AVG_TIME, hdfsDataNodeJson.getDouble(READ_BLOCK_OP_AVG_TIME));
//        hdfsDataNodeMap.put(WRITE_BLOCK_OP_AVG_TIME, hdfsDataNodeJson.getDouble(WRITE_BLOCK_OP_AVG_TIME));
//        hdfsDataNodeMap.put(RPC_QUEUE_TIME_AVG_TIME, hdfsDataNodeRpcJson.getDouble(RPC_QUEUE_TIME_AVG_TIME));
//        hdfsDataNodeMap.put(RPC_PROCESSING_TIME_AVG_TIME, hdfsDataNodeRpcJson.getDouble(RPC_PROCESSING_TIME_AVG_TIME));
//        hdfsDataNodeMap.put(CALL_QUEUE_LENGTH, hdfsDataNodeRpcJson.getDouble(CALL_QUEUE_LENGTH));
//        hdfsDataNodeMap.put(MEM_HEAP_USED_M, hdfsDataNodeJvmJson.getDouble(MEM_HEAP_USED_M));
//        hdfsDataNodeMap.put(THREADS_BLOCKED, hdfsDataNodeJvmJson.getDouble(THREADS_BLOCKED));
//        hdfsDataNodeMap.put(THREADS_WAITING, hdfsDataNodeJvmJson.getDouble(THREADS_WAITING));
//        hdfsDataNodeMap.put(GC_NUM_WARN_THRESHOLD_EXCEEDED, hdfsDataNodeJvmJson.getDouble(GC_NUM_WARN_THRESHOLD_EXCEEDED));
//
//
//        System.out.println(timestamp + " : " + ip);
//        hdfsDataNodeMap.entrySet().forEach(entry -> System.out.println(entry.getKey() + " : " + entry.getValue()));
//    }
//}
