package com.dxz.bigdata.jmx.monitor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dxz.bigdata.jmx.model.ThreadMonitor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.dxz.bigdata.jmx.constant.ItemConstants.*;
import static com.dxz.bigdata.jmx.constant.ObjectConstants.HADOOP_DATA_NODE_JVM_METRICS;
import static com.dxz.bigdata.jmx.constant.ObjectConstants.HADOOP_DATA_NODE_RPC_ACTIVITY_FOR_PORT_50020;
import static com.dxz.bigdata.jmx.utils.MonitorUtil.qryJSonObjectFromJMX;

@Slf4j
public class DataNodesMonitor implements ThreadMonitor {
    @Override
    public HashMap<String, Object> getMap(Map.Entry<Object, Object> entry, CloseableHttpClient httpClient) throws IOException {
        String dataNode = entry.getValue().toString();
        String ip = dataNode.split("/")[2].split(":")[0];
        HashMap<String, Object> map = new HashMap<>();
        //"Hadoop:service=DataNode,name=DataNodeActivity-cu-9e-023-hd0-91-100-50010"
        String HADOOP_DATA_NODE_ACTIVITY = "Hadoop:service=DataNode,name=DataNodeActivity-" + ip + "-50010";
        JSONArray jsonArray = qryJSonObjectFromJMX(httpClient, dataNode);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = jsonObject.getString("name");
            if (name.equals(HADOOP_DATA_NODE_ACTIVITY)) {
                map.put(READ_BLOCK_OP_AVG_TIME, jsonObject.getDouble(READ_BLOCK_OP_AVG_TIME));
                map.put(WRITE_BLOCK_OP_AVG_TIME, jsonObject.getDouble(WRITE_BLOCK_OP_AVG_TIME));
            } else if (name.equals(HADOOP_DATA_NODE_RPC_ACTIVITY_FOR_PORT_50020)) {
                map.put(RPC_QUEUE_TIME_AVG_TIME, jsonObject.getDouble(RPC_QUEUE_TIME_AVG_TIME));
                map.put(RPC_PROCESSING_TIME_AVG_TIME, jsonObject.getDouble(RPC_PROCESSING_TIME_AVG_TIME));
                map.put(CALL_QUEUE_LENGTH, jsonObject.getDouble(CALL_QUEUE_LENGTH));
            } else if (name.equals(HADOOP_DATA_NODE_JVM_METRICS)) {
                map.put(MEM_HEAP_USED_M, jsonObject.getDouble(MEM_HEAP_USED_M));
                map.put(THREADS_BLOCKED, jsonObject.getDouble(THREADS_BLOCKED));
                map.put(THREADS_WAITING, jsonObject.getDouble(THREADS_WAITING));
                map.put(GC_NUM_WARN_THRESHOLD_EXCEEDED, jsonObject.getDouble(GC_NUM_WARN_THRESHOLD_EXCEEDED));
            } else {

            }
        }

        return map;

    }
}