package com.dxz.bigdata.jmx.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dxz.bigdata.jmx.model.InfluxDBConnection;
import com.dxz.bigdata.jmx.model.ThreadMonitor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Desc: 获取JMX接口数据工具类
 * Created by yanlishuai on 2019-11-22
 */
@Slf4j
public class MonitorUtil {
    /**
     * 加载并返回 resources 目录下 名字 为 name 的配置文件
     *
     * @param name 配置文件名称
     * @return 配置文件
     */
    public static Properties getProperties(String name) {
        Properties props = new Properties();
        try {
            props.load(MonitorUtil.class.getClassLoader().getResourceAsStream(name));
        } catch (IOException e) {
            log.error("获取配置文件失败: " + e.getMessage());
        }
        return props;
    }

    /**
     * 获取 InfluxDb 数据库连接
     *
     * @param props influxdb 数据库连接信息 配置文件
     * @return InfluxDb 数据库连接
     */

    public static InfluxDBConnection getInfluxDBConnection(Properties props) {
        //初始化 数据库名称、数据保留策略， 用户名、密码、url等变量
        String database, retentionPolicy, userName, password, url = null;
        if ((database = props.getProperty("DATABASE_NAME", "NIL")).equals("NIL")) {
            log.error("获取 InfluxDB 数据库 失败");
            System.exit(2);
        }
        if ((retentionPolicy = props.getProperty("RETENTION_POLICY", "NIL")).equals("NIL")) {
            log.error("获取 InfluxDB 数据库数据保存策略 失败");
            System.exit(2);
        }
        if ((userName = props.getProperty("USER_NAME", "NIL")).equals("NIL")) {
            log.error("获取 InfluxDB 数据库 用户名 失败");
            System.exit(2);
        }

        if ((password = props.getProperty("PASSWORD", "NIL")).equals("NIL")) {
            log.error("获取 InfluxDB 数据库 用户密码 失败");
            System.exit(2);
        }
        if ((url = props.getProperty("INFLUX_DB_URL", "NIL")).equals("NIL")) {
            log.error("获取 InfluxDB 数据库 url 失败");
            System.exit(2);
        }
        return new InfluxDBConnection(userName, password, url, database, retentionPolicy);

    }

    /**
     * 通过线程池启用多线程获取所有DataNode监控指标
     *
     * @param props          配置文件
     * @param httpClient     httpCelient 对象
     * @param monitor        监控指标获取类 实例对象
     * @param countDownLatch 计数器
     * @throws InterruptedException
     */
    public static HashMap<String, HashMap<String, Object>> executeThreads(Properties props1, CloseableHttpClient httpClient, ThreadMonitor monitor, CountDownLatch countDownLatch) throws InterruptedException {


        //创建固定大小线程池,大小为 DataNode 数量
        ExecutorService executorService = Executors.newFixedThreadPool(props1.size());

        // 将不同的batchPoints序列化后，一次性写入数据库，提高写入速度
        HashMap<String, HashMap<String, Object>> map = new HashMap<>(3);
        //循环DataNode JMX URL 列表 向 线程池提交任务
        props1.entrySet().forEach(entry -> {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String[] url = entry.getValue().toString().split("/");
                        String host = null;
                        if (url.length > 1) {
                             host = url[2].split(":")[0];
                        } else {
                             host = entry.getValue().toString();
                        }
                        log.info("host : {}", host);
                        map.put(host, monitor.getMap(entry, httpClient));
//                        records.add(batchPoints.lineProtocol());
                    } catch (NullPointerException | IOException e) {
                        e.printStackTrace();
                    } finally {
                        //计数器减1
                        countDownLatch.countDown();
                        log.info(entry.getValue().toString() + "线程执行完毕！");
                    }

                }
            });

        });
        //等待所有子线程执行完毕，返回
        countDownLatch.await();
        //关闭线程池
        executorService.shutdown();
        log.info("获取指标数据完成");
        return map;
    }

    /**
     * 向 InfluxDb中批量插入数据
     *
     * @param influxDBConnection influxdb 数据库连接
     * @param map                监控指标数据 key 为host map 存放具体监控指标数据
     */
    public static void insertIntoInfliuxDb(InfluxDBConnection influxDBConnection, HashMap<String, HashMap<String, Object>> map, String measurement) {
        Long timestamp = DateUtil.getTimestampMil();
        List<String> records = new ArrayList<>(3);
        System.out.println(map.size());
        for (HashMap.Entry<String, HashMap<String, Object>> entry : map.entrySet()) {
            log.info("构建标签");
            Map<String, String> tags = new HashMap<String, String>();
            tags.put("host", entry.getKey());
            log.info("创建一条记录");
            Point point = influxDBConnection.pointBuilder(measurement, timestamp, tags, entry.getValue());
            BatchPoints batchPoints = BatchPoints.database(influxDBConnection.getDatabase()).tag("host",entry.getKey())
                    .retentionPolicy(influxDBConnection.getRetentionPolicy()).consistency(InfluxDB.ConsistencyLevel.ALL).build();
            batchPoints.point(point);
            records.add(batchPoints.lineProtocol());
        }
        log.info("开始向influxdb 中插入获取的指标数据");
        influxDBConnection.batchInsert(influxDBConnection.getDatabase(), influxDBConnection.getRetentionPolicy(), InfluxDB.ConsistencyLevel.ALL,
                records);

    }

    /**
     * 获取JMX监控数据并存入 JSON
     *
     * @param URL        JMX接口url
     * @param objectName 获取的监控项名称
     * @return json
     * @throws IOException
     */
    public static JSONArray qryJSonObjectFromJMX(CloseableHttpClient httpClient, String URL) throws IOException {
        JSONArray jsonArray = null;
        ByteArrayOutputStream result = null;
        InputStream inputStream = null;
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpGet = new HttpGet(URL);
            httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.info(URL + " JMX 接口请求成功: " + statusCode);
            } else {
                log.error(URL + " JMX 接口请求失败: " + statusCode);
            }
            result = new ByteArrayOutputStream();
            inputStream = httpResponse.getEntity().getContent();
            byte[] buffer = new byte[2048];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String jsonStr = result.toString("UTF-8");
            jsonArray = JSONObject.parseObject(jsonStr).getJSONArray("beans");
        } catch (NullPointerException e) {
            log.error(URL + " 获取JMX数据失败: " + e.getMessage());
        } finally {
            try {
                if (null != httpResponse) {
                    httpResponse.close();
                }
                if (null != result) {
                    result.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("HTTP 连接关闭异常: {}", (Object) e.getStackTrace());
            }
        }
        log.info(URL + " 成功获取JMX数据并转换为JSON!");
        return jsonArray;
    }
}
