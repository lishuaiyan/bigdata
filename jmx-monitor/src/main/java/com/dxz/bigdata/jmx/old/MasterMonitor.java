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
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import static com.dxz.bigdata.jmx.constant.ItemConstants.*;
//import static com.dxz.bigdata.jmx.constant.ObjectConstants.*;
//import static com.dxz.bigdata.jmx.utils.JmxUtil.qryJSonObjectFromJMX;
//
///**
// * Desc: HBase Master 关键指标监控
// * created by yanlishuai on 2019-11-22
// */
//@Slf4j
//public class MasterMonitor {
//    private static volatile CountDownLatch countDownLatch = new CountDownLatch(3);
//
//    public static void main(String[] args) throws IOException, InterruptedException {
//        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//        cm.setMaxTotal(15);
//        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
//        Properties prop = new Properties();
//        try {
//            prop.load(MasterMonitor.class.getClassLoader().getResourceAsStream("master.properties"));
//        } catch(IOException e) {
//            log.error("获取配置文件失败: " + e.getMessage());
//        }
//
////        String HMaster = prop.getProperty("HBASE_MASTER_SERVER");
//        while (true) {
//            executeThreads(prop, httpClient);
//            Thread.sleep(10000);
//        }
//
////        System.out.println("HBase RegionServers 数量: " + hbaseServerJson.getLong("numRegionServers"));
////        System.out.println("HBase 每个regionServer平均region数目: " + hbaseServerJson.getLong("averageLoad"));
////        System.out.println("所有regionServer总请求数量: " + hbaseServerJson.getLong("clusterRequests"));
////        System.out.println("状态不一致region个数: " + hbaseAssignJson.getLong("ritCount"));
////        System.out.println("转换超时的region个数:" + hbaseAssignJson.getLong("ritCountOverThreshold"));
//
//    }
//    private static void executeThreads(Properties props, CloseableHttpClient httpClient) throws InterruptedException {
//        Long startTime = DateUtil.getTimestampMil();
//        ExecutorService executorService = Executors.newFixedThreadPool(props.size());
//        props.entrySet().forEach(entry -> {
//            executorService.submit(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        switch (entry.getKey().toString()) {
//                            case "HBASE_MASTER_SERVER" :
//                                getHbaseMap(entry.getValue().toString(), startTime, httpClient);
//                            case "YARN_RESOURCE_MANAGER" :
//                                getYarnMap(entry.getValue().toString(), startTime, httpClient);
//                            case "HDFS_NAME_NODE" :
//                                getHdfsMap(entry.getValue().toString(), startTime, httpClient);
// //                           default:
// //                               log.error("配置文件错误， error key = {}, error value = {}", entry.getKey(), entry.getValue());
//                        }
//
//                    } catch (IOException e) {
//                        log.error("获取配置文件中 JMX URL 失败 {}", e.getCause());
//                    } finally {
//                        countDownLatch.countDown();
//                        log.info(entry.getValue().toString() + "线程执行完毕！");
//                    }
//
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
//    private static void getHdfsMap(String Master, Long timestamp, CloseableHttpClient httpClient) throws IOException {
//        Map<String, Double> nameNodeMap = new HashMap<>();
//
//        JSONObject nameNodeInfoJson = qryJSonObjectFromJMX(httpClient, Master, HADOOP_NAME_NODE_INFO);
//        JSONObject nameNodeSystemState = qryJSonObjectFromJMX(httpClient, Master, HADOOP_NAME_NODE_FS_NAME_SYSTEM_STATE);
//        JSONObject nameNodeJvmJson = qryJSonObjectFromJMX(httpClient, Master, HADOOP_NAME_NODE_JVM_METRICS);
//        JSONObject nameNodeRpc8022 = qryJSonObjectFromJMX(httpClient, Master, HADOOP_NAME_NODE_RPC_ACTIVITY_FOR_PORT_8022);
//        JSONObject nameNodeRpc8020 = qryJSonObjectFromJMX(httpClient, Master, HADOOP_NAME_NODE_RPC_ACTIVITY_FOR_PORT_8020);
//        nameNodeMap.put(TOTAL_FILES, nameNodeInfoJson.getDouble(TOTAL_FILES));
//        nameNodeMap.put(TOTAL_BLOCKS, nameNodeInfoJson.getDouble(TOTAL_BLOCKS));
//        nameNodeMap.put(PERCENT_USED, nameNodeInfoJson.getDouble(PERCENT_USED));
//        nameNodeMap.put(NUM_LIVE_DATA_NODES, nameNodeSystemState.getDouble(NUM_LIVE_DATA_NODES));
//        nameNodeMap.put(NUM_DEAD_DATA_NODES, nameNodeSystemState.getDouble(NUM_DEAD_DATA_NODES));
//        nameNodeMap.put(VOLUME_FAILURES_TOTAL, nameNodeSystemState.getDouble(VOLUME_FAILURES_TOTAL));
//        nameNodeMap.put(NUMBER_OF_MISSING_BLOCKS, nameNodeInfoJson.getDouble(NUMBER_OF_MISSING_BLOCKS));
//        nameNodeMap.put(RPC_QUEUE_TIME_AVG_TIME + "_8022", nameNodeRpc8022.getDouble(RPC_QUEUE_TIME_AVG_TIME));
//        nameNodeMap.put(RPC_QUEUE_TIME_AVG_TIME + "_8020", nameNodeRpc8020.getDouble(RPC_QUEUE_TIME_AVG_TIME));
//        nameNodeMap.put(RPC_PROCESSING_TIME_AVG_TIME + "_8022", nameNodeRpc8022.getDouble(RPC_PROCESSING_TIME_AVG_TIME));
//        nameNodeMap.put(RPC_PROCESSING_TIME_AVG_TIME + "_8020", nameNodeRpc8020.getDouble(RPC_PROCESSING_TIME_AVG_TIME));
//        nameNodeMap.put(CALL_QUEUE_LENGTH + "_8022", nameNodeRpc8022.getDouble(CALL_QUEUE_LENGTH));
//        nameNodeMap.put(CALL_QUEUE_LENGTH + "_8020", nameNodeRpc8020.getDouble(CALL_QUEUE_LENGTH));
//        nameNodeMap.put(MEM_HEAP_USED_M, nameNodeJvmJson.getDouble(MEM_HEAP_USED_M));
//        nameNodeMap.put(THREADS_BLOCKED, nameNodeJvmJson.getDouble(THREADS_BLOCKED));
//        nameNodeMap.put(THREADS_WAITING, nameNodeJvmJson.getDouble(THREADS_WAITING));
//        nameNodeMap.put(GC_NUM_WARN_THRESHOLD_EXCEEDED, nameNodeJvmJson.getDouble(GC_NUM_WARN_THRESHOLD_EXCEEDED));
//        System.out.println("输出HDFS NameNode 监控指标 " + timestamp);
//        nameNodeMap.entrySet().forEach(entry -> System.out.println(entry.getKey() + ":" + entry.getValue()));
//    }
//    private static void getYarnMap(String Master, Long timestamp, CloseableHttpClient httpClient) throws IOException {
//        Map<String, Double> yarnMap = new HashMap<>();
//
//        JSONObject yarnJson = qryJSonObjectFromJMX(httpClient, Master, YARN_RESOURCE_MANAGER_CLUSTER_MRTRICS);
//        JSONObject superJson = qryJSonObjectFromJMX(httpClient, Master, YARN_RESOURCE_MANAGER_QUEUE_METRICS_SUPER);
//        JSONObject defaultJson = qryJSonObjectFromJMX(httpClient, Master, YARN_RESOURCE_MANAGER_QUEUE_METRICS_DEFAULT);
//        JSONObject ottJson = qryJSonObjectFromJMX(httpClient, Master, YARN_RESOURCE_MANAGER_QUEUE_METRICS_OTT);
//        JSONObject fsJosn = qryJSonObjectFromJMX(httpClient, Master, YARN_RESOURCE_MANAGER_FS_OP_DURATIONS);
//        yarnMap.put(NUM_ACTIVE_NMS, yarnJson.getDouble(NUM_ACTIVE_NMS));
//        yarnMap.put(NUM_UNHEALTHY_NMS, yarnJson.getDouble(NUM_UNHEALTHY_NMS));
//        yarnMap.put(NUM_LOST_NMS, yarnJson.getDouble(NUM_LOST_NMS));
//        yarnMap.put(NODE_UPDATE_CALL_AVG_TIME, fsJosn.getDouble(NODE_UPDATE_CALL_AVG_TIME));
//        yarnMap.put(NODE_UPDATE_CALL_NUM_OPS, fsJosn.getDouble(NODE_UPDATE_CALL_AVG_TIME));
//        Map<String, Double> superMap = getQueueMetrics(superJson);
//        Map<String, Double> defaultMap = getQueueMetrics(defaultJson);
//        Map<String, Double> ottMap = getQueueMetrics(ottJson);
//        System.out.println("输出 YARN 监控指标 " + timestamp);
//        yarnMap.entrySet().forEach(entry -> System.out.println(entry.getKey() + " : " + entry.getValue()));
//        superMap.entrySet().forEach(entry -> System.out.println(entry.getKey() + " : " + entry.getValue()));
//        defaultMap.entrySet().forEach(entry -> System.out.println(entry.getKey() + " : " + entry.getValue()));
//        ottMap.entrySet().forEach(entry -> System.out.println(entry.getKey() + " : " + entry.getValue()));
//
//    }
//    private static void getHbaseMap(String HMaster, Long timestamp, CloseableHttpClient httpClient) throws IOException {
//        String host = HMaster.split("/")[2].split(":")[0];
//        Map<String, Object> hbaseMasterMap = new HashMap<>();
//        JSONObject hbaseServerJson = qryJSonObjectFromJMX(httpClient, HMaster, HBASE_MASTER_SERVER);
//        JSONObject hbaseAssignJson = qryJSonObjectFromJMX(httpClient, HMaster, HBASE_MASTER_ASSIGNMENT_MANGER);
//        hbaseMasterMap.put(NUM_REGION_SERVERS, hbaseServerJson.getLong(NUM_REGION_SERVERS));
//        hbaseMasterMap.put(AVERAGE_LOAD, hbaseServerJson.getLong(AVERAGE_LOAD));
//        hbaseMasterMap.put(CLUSTER_REQUESTS, hbaseServerJson.getLong(CLUSTER_REQUESTS));
//        hbaseMasterMap.put(RIT_COUNT, hbaseAssignJson.getLong(RIT_COUNT));
//        hbaseMasterMap.put(RIT_COUNT_OVER_THRESHOLD, hbaseAssignJson.getLong(RIT_COUNT_OVER_THRESHOLD));
//        System.out.println("数据HABSE 监控指标 " + timestamp);
//        log.info("开始向 Influxdb 写入数据！！！");
//        InfluxDbUtil.insertInflux(hbaseMasterMap, timestamp, host,"hbaseMaster");
//        log.info("向 influxd 插入数据完成");
//        hbaseMasterMap.entrySet().forEach(entry -> System.out.println(entry.getKey() + " : " + entry.getValue()));
//    }
//    private static Map<String, Double> getQueueMetrics(JSONObject jsonObject) {
//        Map<String, Double> map = new HashMap<>();
//        map.put(APPS_SUBMITTED, jsonObject.getDouble(APPS_SUBMITTED));
//        map.put(APPS_PENDING, jsonObject.getDouble(APPS_PENDING));
//        map.put(APPS_RUNNING, jsonObject.getDouble(APPS_RUNNING));
//        map.put(APPS_COMPLETED, jsonObject.getDouble(APPS_COMPLETED));
//        map.put(APPS_FAILED, jsonObject.getDouble(APPS_FAILED));
//        map.put(APPS_KILLED, jsonObject.getDouble(APPS_KILLED));
//        map.put(ALLOCATED_MB, jsonObject.getDouble(ALLOCATED_MB));
//        map.put(ALLOCATED_CONTAINERS, jsonObject.getDouble(ALLOCATED_CONTAINERS));
//        map.put(ALLOCATED_V_CORES, jsonObject.getDouble(ALLOCATED_V_CORES));
//        map.put(AVAILABLE_MB, jsonObject.getDouble(AVAILABLE_MB));
//        map.put(AVAILABLE_V_CORES, jsonObject.getDouble(AVAILABLE_V_CORES));
//        map.put(PENDING_MB, jsonObject.getDouble(PENDING_MB));
//        map.put(PENDING_V_CORES, jsonObject.getDouble(PENDING_V_CORES));
//        map.put(PENDING_CONTAINERS, jsonObject.getDouble(PENDING_CONTAINERS));
//        map.put(RESERVED_MB, jsonObject.getDouble(RESERVED_MB));
//        map.put(RESERVED_V_CORES, jsonObject.getDouble(RESERVED_V_CORES));
//        map.put(RESERVED_CONTAINERS, jsonObject.getDouble(RESERVED_CONTAINERS));
//        return map;
//    }
//
//
//}
