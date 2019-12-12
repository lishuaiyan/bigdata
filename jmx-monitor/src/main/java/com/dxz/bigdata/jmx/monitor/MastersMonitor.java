package com.dxz.bigdata.jmx.monitor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dxz.bigdata.jmx.model.ThreadMonitor;
import com.dxz.bigdata.jmx.old.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.dxz.bigdata.jmx.constant.ItemConstants.*;
import static com.dxz.bigdata.jmx.constant.ObjectConstants.*;
import static com.dxz.bigdata.jmx.utils.MonitorUtil.qryJSonObjectFromJMX;


@Slf4j
public class MastersMonitor implements ThreadMonitor {

    private static HashMap<String, Object> getHdfsMap(String Master, CloseableHttpClient httpClient) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        String host = Master.split("/")[2].split(":")[0];
        JSONArray jsonArray = qryJSonObjectFromJMX(httpClient, Master);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            switch (jsonObject.getString("name")) {
                case HADOOP_NAME_NODE_INFO:
                    map.put(TOTAL_FILES, jsonObject.getDouble(TOTAL_FILES));
                    map.put(TOTAL_BLOCKS, jsonObject.getDouble(TOTAL_BLOCKS));
                    map.put(PERCENT_USED, jsonObject.getDouble(PERCENT_USED));
                    map.put(NUMBER_OF_MISSING_BLOCKS, jsonObject.getDouble(NUMBER_OF_MISSING_BLOCKS));
                    break;
                case HADOOP_NAME_NODE_FS_NAME_SYSTEM_STATE:
                    map.put(NUM_LIVE_DATA_NODES, jsonObject.getDouble(NUM_LIVE_DATA_NODES));
                    map.put(NUM_DEAD_DATA_NODES, jsonObject.getDouble(NUM_DEAD_DATA_NODES));
                    map.put(VOLUME_FAILURES_TOTAL, jsonObject.getDouble(VOLUME_FAILURES_TOTAL));
                    break;
                case HADOOP_NAME_NODE_JVM_METRICS:
                    map.put(MEM_HEAP_USED_M, jsonObject.getDouble(MEM_HEAP_USED_M));
                    map.put(THREADS_BLOCKED, jsonObject.getDouble(THREADS_BLOCKED));
                    map.put(THREADS_WAITING, jsonObject.getDouble(THREADS_WAITING));
                    map.put(GC_NUM_WARN_THRESHOLD_EXCEEDED, jsonObject.getDouble(GC_NUM_WARN_THRESHOLD_EXCEEDED));
                    break;
                case HADOOP_NAME_NODE_RPC_ACTIVITY_FOR_PORT_8022:
                    map.put(RPC_QUEUE_TIME_AVG_TIME + "_8022", jsonObject.getDouble(RPC_QUEUE_TIME_AVG_TIME));
                    map.put(RPC_PROCESSING_TIME_AVG_TIME + "_8022", jsonObject.getDouble(RPC_PROCESSING_TIME_AVG_TIME));
                    map.put(CALL_QUEUE_LENGTH + "_8022", jsonObject.getDouble(CALL_QUEUE_LENGTH));
                    break;
                case HADOOP_NAME_NODE_RPC_ACTIVITY_FOR_PORT_8020:
                    map.put(RPC_QUEUE_TIME_AVG_TIME + "_8020", jsonObject.getDouble(RPC_QUEUE_TIME_AVG_TIME));
                    map.put(RPC_PROCESSING_TIME_AVG_TIME + "_8020", jsonObject.getDouble(RPC_PROCESSING_TIME_AVG_TIME));
                    map.put(CALL_QUEUE_LENGTH + "_8020", jsonObject.getDouble(CALL_QUEUE_LENGTH));
                    break;
                default:
                    break;
            }
        }
        map.entrySet().forEach(stringObjectEntry -> {
            System.out.println(stringObjectEntry.toString());
        });
        return map;
    }

    private static HashMap<String, Object> getHbaseMap(String HMaster, CloseableHttpClient httpClient) throws IOException {
        String host = HMaster.split("/")[2].split(":")[0];
        HashMap<String, Object> map = new HashMap<>();
        JSONArray jsonArray = Utils.qryJSonObjectFromJMX(httpClient, HMaster);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            switch (jsonObject.getString("name")) {
                case HBASE_MASTER_SERVER:
                    System.out.println(jsonObject.getString("name"));
                    map.put(NUM_REGION_SERVERS, jsonObject.getLong(NUM_REGION_SERVERS));
                    map.put(AVERAGE_LOAD, jsonObject.getLong(AVERAGE_LOAD));
                    map.put(CLUSTER_REQUESTS, jsonObject.getLong(CLUSTER_REQUESTS));
                    break;
                case HBASE_MASTER_ASSIGNMENT_MANGER:
                    System.out.println(jsonObject.getString("name"));
                    map.put(RIT_COUNT, jsonObject.getLong(RIT_COUNT));
                    map.put(RIT_COUNT_OVER_THRESHOLD, jsonObject.getLong(RIT_COUNT_OVER_THRESHOLD));
                    break;
                default:
                    break;
            }
        }
        map.entrySet().forEach(stringObjectEntry -> {
            System.out.println(stringObjectEntry.toString());
        });
        return map;
    }

    private static HashMap<String, Object> getYarnMap(String Master, CloseableHttpClient httpClient) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        String host = Master.split("/")[2].split(":")[0];
        JSONArray jsonArray = qryJSonObjectFromJMX(httpClient, Master);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            switch (jsonObject.getString("name")) {
                case YARN_RESOURCE_MANAGER_CLUSTER_MRTRICS:
                    map.put(NUM_ACTIVE_NMS, jsonObject.getDouble(NUM_ACTIVE_NMS));
                    map.put(NUM_UNHEALTHY_NMS, jsonObject.getDouble(NUM_UNHEALTHY_NMS));
                    map.put(NUM_LOST_NMS, jsonObject.getDouble(NUM_LOST_NMS));
                    break;
                case YARN_RESOURCE_MANAGER_QUEUE_METRICS_SUPER:
                    map.put(APPS_SUBMITTED + SUPER_QUEUE, jsonObject.getDouble(APPS_SUBMITTED));
                    map.put(APPS_PENDING + SUPER_QUEUE, jsonObject.getDouble(APPS_PENDING));
                    map.put(APPS_RUNNING + SUPER_QUEUE, jsonObject.getDouble(APPS_RUNNING));
                    map.put(APPS_COMPLETED + SUPER_QUEUE, jsonObject.getDouble(APPS_COMPLETED));
                    map.put(APPS_FAILED + SUPER_QUEUE, jsonObject.getDouble(APPS_FAILED));
                    map.put(APPS_KILLED + SUPER_QUEUE, jsonObject.getDouble(APPS_KILLED));
                    map.put(ALLOCATED_MB + SUPER_QUEUE, jsonObject.getDouble(ALLOCATED_MB));
                    map.put(ALLOCATED_CONTAINERS + SUPER_QUEUE, jsonObject.getDouble(ALLOCATED_CONTAINERS));
                    map.put(ALLOCATED_V_CORES + SUPER_QUEUE, jsonObject.getDouble(ALLOCATED_V_CORES));
                    map.put(AVAILABLE_MB + SUPER_QUEUE, jsonObject.getDouble(AVAILABLE_MB));
                    map.put(AVAILABLE_V_CORES + SUPER_QUEUE, jsonObject.getDouble(AVAILABLE_V_CORES));
                    map.put(PENDING_MB + SUPER_QUEUE, jsonObject.getDouble(PENDING_MB));
                    map.put(PENDING_V_CORES + SUPER_QUEUE, jsonObject.getDouble(PENDING_V_CORES));
                    map.put(PENDING_CONTAINERS + SUPER_QUEUE, jsonObject.getDouble(PENDING_CONTAINERS));
                    map.put(RESERVED_MB + SUPER_QUEUE, jsonObject.getDouble(RESERVED_MB));
                    map.put(RESERVED_V_CORES + SUPER_QUEUE, jsonObject.getDouble(RESERVED_V_CORES));
                    map.put(RESERVED_CONTAINERS + SUPER_QUEUE, jsonObject.getDouble(RESERVED_CONTAINERS));
                    break;
                case YARN_RESOURCE_MANAGER_QUEUE_METRICS_DEFAULT:
                    map.put(APPS_SUBMITTED + DEFAULT_QUEUE, jsonObject.getDouble(APPS_SUBMITTED));
                    map.put(APPS_PENDING + DEFAULT_QUEUE, jsonObject.getDouble(APPS_PENDING));
                    map.put(APPS_RUNNING + DEFAULT_QUEUE, jsonObject.getDouble(APPS_RUNNING));
                    map.put(APPS_COMPLETED + DEFAULT_QUEUE, jsonObject.getDouble(APPS_COMPLETED));
                    map.put(APPS_FAILED + DEFAULT_QUEUE, jsonObject.getDouble(APPS_FAILED));
                    map.put(APPS_KILLED + DEFAULT_QUEUE, jsonObject.getDouble(APPS_KILLED));
                    map.put(ALLOCATED_MB + DEFAULT_QUEUE, jsonObject.getDouble(ALLOCATED_MB));
                    map.put(ALLOCATED_CONTAINERS + DEFAULT_QUEUE, jsonObject.getDouble(ALLOCATED_CONTAINERS));
                    map.put(ALLOCATED_V_CORES + DEFAULT_QUEUE, jsonObject.getDouble(ALLOCATED_V_CORES));
                    map.put(AVAILABLE_MB + DEFAULT_QUEUE, jsonObject.getDouble(AVAILABLE_MB));
                    map.put(AVAILABLE_V_CORES + DEFAULT_QUEUE, jsonObject.getDouble(AVAILABLE_V_CORES));
                    map.put(PENDING_MB + DEFAULT_QUEUE, jsonObject.getDouble(PENDING_MB));
                    map.put(PENDING_V_CORES + DEFAULT_QUEUE, jsonObject.getDouble(PENDING_V_CORES));
                    map.put(PENDING_CONTAINERS + DEFAULT_QUEUE, jsonObject.getDouble(PENDING_CONTAINERS));
                    map.put(RESERVED_MB + DEFAULT_QUEUE, jsonObject.getDouble(RESERVED_MB));
                    map.put(RESERVED_V_CORES + DEFAULT_QUEUE, jsonObject.getDouble(RESERVED_V_CORES));
                    map.put(RESERVED_CONTAINERS + DEFAULT_QUEUE, jsonObject.getDouble(RESERVED_CONTAINERS));
                    break;
                case YARN_RESOURCE_MANAGER_QUEUE_METRICS_OTT:
                    map.put(APPS_SUBMITTED + OTT_QUEUE, jsonObject.getDouble(APPS_SUBMITTED));
                    map.put(APPS_PENDING + OTT_QUEUE, jsonObject.getDouble(APPS_PENDING));
                    map.put(APPS_RUNNING + OTT_QUEUE, jsonObject.getDouble(APPS_RUNNING));
                    map.put(APPS_COMPLETED + OTT_QUEUE, jsonObject.getDouble(APPS_COMPLETED));
                    map.put(APPS_FAILED + OTT_QUEUE, jsonObject.getDouble(APPS_FAILED));
                    map.put(APPS_KILLED + OTT_QUEUE, jsonObject.getDouble(APPS_KILLED));
                    map.put(ALLOCATED_MB + OTT_QUEUE, jsonObject.getDouble(ALLOCATED_MB));
                    map.put(ALLOCATED_CONTAINERS + OTT_QUEUE, jsonObject.getDouble(ALLOCATED_CONTAINERS));
                    map.put(ALLOCATED_V_CORES + OTT_QUEUE, jsonObject.getDouble(ALLOCATED_V_CORES));
                    map.put(AVAILABLE_MB + OTT_QUEUE, jsonObject.getDouble(AVAILABLE_MB));
                    map.put(AVAILABLE_V_CORES + OTT_QUEUE, jsonObject.getDouble(AVAILABLE_V_CORES));
                    map.put(PENDING_MB + OTT_QUEUE, jsonObject.getDouble(PENDING_MB));
                    map.put(PENDING_V_CORES + OTT_QUEUE, jsonObject.getDouble(PENDING_V_CORES));
                    map.put(PENDING_CONTAINERS + OTT_QUEUE, jsonObject.getDouble(PENDING_CONTAINERS));
                    map.put(RESERVED_MB + OTT_QUEUE, jsonObject.getDouble(RESERVED_MB));
                    map.put(RESERVED_V_CORES + OTT_QUEUE, jsonObject.getDouble(RESERVED_V_CORES));
                    map.put(RESERVED_CONTAINERS + OTT_QUEUE, jsonObject.getDouble(RESERVED_CONTAINERS));
                    break;
                case YARN_RESOURCE_MANAGER_FS_OP_DURATIONS:
                    map.put(NODE_UPDATE_CALL_AVG_TIME, jsonObject.getDouble(NODE_UPDATE_CALL_AVG_TIME));
                    map.put(NODE_UPDATE_CALL_NUM_OPS, jsonObject.getDouble(NODE_UPDATE_CALL_NUM_OPS));
                    break;
                default:
                    break;
            }
        }
        map.entrySet().forEach(stringObjectEntry -> {
            System.out.println(stringObjectEntry.toString());
        });
        System.out.println("111111111111111111111111");
        return map;
    }

    @Override
    public HashMap<String, Object> getMap(Map.Entry<Object, Object> entry, CloseableHttpClient httpClient) throws IOException {
        HashMap<String, Object> map = null;

        switch (entry.getKey().toString()) {
            case "HBASE_MASTER_SERVER":
                map = getHbaseMap(entry.getValue().toString(), httpClient);
                break;
            case "YARN_RESOURCE_MANAGER":
                map = getYarnMap(entry.getValue().toString(), httpClient);
                break;
            case "HDFS_NAME_NODE":
                map = getHdfsMap(entry.getValue().toString(), httpClient);
                break;
            default:
                break;
        }
        return map;
    }
}
