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
import static com.dxz.bigdata.jmx.constant.ObjectConstants.*;
import static com.dxz.bigdata.jmx.utils.MonitorUtil.qryJSonObjectFromJMX;

@Slf4j
public class RegionServersMonitor implements ThreadMonitor {
    @Override
    public HashMap<String, Object> getMap(Map.Entry<Object, Object> entry, CloseableHttpClient httpClient) throws IOException {
        String HRegionServer = entry.getValue().toString();
        String ip = HRegionServer.split("/")[2].split(":")[0];
        HashMap<String, Object> map = new HashMap<>();
        JSONArray jsonArray = qryJSonObjectFromJMX(httpClient, HRegionServer);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            switch (jsonObject.getString("name")) {
                case HBASE_REGION_SERVER_SERVER:
                    map.put(REGION_COUNT, jsonObject.getLong(REGION_COUNT));
                    map.put("SERVER_" + STORE_FILE_SIZE, jsonObject.getLong(STORE_FILE_SIZE));
                    map.put("SERVER_" + STORE_FILE_COUNT, jsonObject.getLong(STORE_FILE_COUNT));
                    map.put(HLOG_FILE_COUNT, jsonObject.getLong(HLOG_FILE_COUNT));
                    map.put(TOTAL_REQUEST_COUNT, jsonObject.getLong(TOTAL_REQUEST_COUNT));
                    map.put("SERVER_" + READ_REQUEST_COUNT, jsonObject.getLong(READ_REQUEST_COUNT));
                    map.put("SERVER_" + WRITE_REQUEST_COUNT, jsonObject.getLong(WRITE_REQUEST_COUNT));
                    map.put(FLUSH_QUEUE_LENGTH, jsonObject.getLong(FLUSH_QUEUE_LENGTH));
                    map.put(COMPACTION_QUEUE_LENGTH, jsonObject.getLong(COMPACTION_QUEUE_LENGTH));
                    map.put(MEM_STORE_SIZE, jsonObject.getLong(MEM_STORE_SIZE));
                    map.put(UPDATES_BLOCKED_TIME, jsonObject.getLong(UPDATES_BLOCKED_TIME));
                    map.put(BLOCK_CACHE_HIT_COUNT, jsonObject.getLong(BLOCK_CACHE_HIT_COUNT));
                    map.put(BLOCK_CACHE_MISS_COUNT, jsonObject.getLong(BLOCK_CACHE_MISS_COUNT));
                    map.put(BLOCK_CACHE_EXPRESS_HIT_PERCENT, jsonObject.getLong(BLOCK_CACHE_EXPRESS_HIT_PERCENT));
                    map.put(PERCENT_FILES_LOCAL, jsonObject.getLong(PERCENT_FILES_LOCAL));
                    map.put(SLOW_GET_COUNT, jsonObject.getLong(SLOW_GET_COUNT));
                    break;
//                case HBASE_REGION_SERVER_REGIONS:
//                    break;
                case HBASE_REGION_SERVER_IPC:
                    map.put(NUM_OPEN_CONNECTIONS, jsonObject.getLong(NUM_OPEN_CONNECTIONS));
                    map.put(NUM_ACTIVE_HANDLER, jsonObject.getLong(NUM_ACTIVE_HANDLER));
                    break;
                case HBASE_JVM_METRICS:
                    map.put(GC_TIME_MILLIS, jsonObject.getLong(GC_TIME_MILLIS));
                    map.put(GC_TIME_MILLIS_PAR_NEW, jsonObject.getLong(GC_TIME_MILLIS_PAR_NEW));
                    map.put(GC_TIME_MILLIS_CONCURRENT_MARK_SWEEP, jsonObject.getLong(GC_TIME_MILLIS_CONCURRENT_MARK_SWEEP));
                    break;
                default:
                    break;
            }
        }

        return map;
    }

}
