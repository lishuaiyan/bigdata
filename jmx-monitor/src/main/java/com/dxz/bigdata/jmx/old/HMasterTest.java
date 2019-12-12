package com.dxz.bigdata.jmx.old;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dxz.bigdata.jmx.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.dxz.bigdata.jmx.constant.ItemConstants.*;
import static com.dxz.bigdata.jmx.constant.ObjectConstants.HBASE_MASTER_ASSIGNMENT_MANGER;
import static com.dxz.bigdata.jmx.constant.ObjectConstants.HBASE_MASTER_SERVER;
import static com.dxz.bigdata.jmx.old.Utils.qryJSonObjectFromJMX;

@Slf4j
public class HMasterTest {
    public static void main(String[] args) throws IOException {
        //初始化HTTP 连接池，可以同时为多个线程服务
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        //设置连接池的最大连接数为15
        //cm.setMaxPerRoute();
        cm.setMaxTotal(15);
        //使用CloseableHttpClient
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        Long startTime = DateUtil.getTimestampMil();
        getHbaseMap("http://cu-9e-023-hm0-91-60:60010/jmx", startTime, httpClient);

    }

    private static void getHbaseMap(String HMaster, Long timestamp, CloseableHttpClient httpClient) throws IOException {
        String host = HMaster.split("/")[2].split(":")[0];
        Map<String, Object> map = new HashMap<>();
        JSONArray jsonArray = qryJSonObjectFromJMX(httpClient, HMaster);
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
            System.out.println(stringObjectEntry.getKey() + ":" + stringObjectEntry.getValue());
        });


    }

}
