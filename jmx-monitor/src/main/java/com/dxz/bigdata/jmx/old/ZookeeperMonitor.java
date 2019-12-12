package com.dxz.bigdata.jmx.old;

import com.dxz.bigdata.jmx.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.dxz.bigdata.jmx.constant.ItemConstants.*;

@Slf4j
public class ZookeeperMonitor {
    private static volatile CountDownLatch countDownLatch = new CountDownLatch(3);
    public static void main(String[] args) throws IOException, InterruptedException {
        Properties prop = new Properties();
        try {
            prop.load(ZookeeperMonitor.class.getClassLoader().getResourceAsStream("zookeeper.properties"));
        } catch(IOException e) {
            log.error("获取配置文件失败: " + e.getMessage());
        }
        executeThreads(prop);
    }
    private static void executeThreads(Properties props) throws InterruptedException {
        Long startTime = DateUtil.getTimestampMil();
        ExecutorService executorService = Executors.newFixedThreadPool(props.size());
        props.entrySet().forEach(entry -> {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        serverMntr(entry.getValue().toString());
                    } catch (IOException e) {
                        log.error("{} 获取 Zookeeper 监控数据异常 {}", entry.getKey(), e.getCause());
                    }
                }
            });

        });
        //等待所有子线程执行完毕，返回
        countDownLatch.await();
        Long endTime = DateUtil.getTimestampMil();
        System.out.println("共耗时： " + String.valueOf(endTime - startTime) + " s");
        executorService.shutdown();
    }
    public static void serverMntr(String host) throws IOException {
        String cmd = "mntr";
        Map<String, Long> map = new HashMap<>();
        Socket socket = new Socket(host, 2181);
        BufferedReader reader = null;
        try {
            OutputStream outputStream = socket.getOutputStream();
            //通过Zookeeper的四字命令获取服务器的状态
            outputStream.write(cmd.getBytes());
            outputStream.flush();
            socket.shutdownOutput();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] s = line.split("\\+s");
                switch (s[0]) {
                    case ZK_SERVER_STATE:
                        map.put(ZK_SERVER_STATE, Long.parseLong(s[1]));
                    case ZK_FOLLOWERS:
                        map.put(ZK_FOLLOWERS, Long.parseLong(s[1]));
                    case ZK_SYNCED_FOLLOWERS:
                        map.put(ZK_SYNCED_FOLLOWERS, Long.parseLong(s[1]));
                    case ZK_OUTSTANDING_REQUESTS:
                        map.put(ZK_OUTSTANDING_REQUESTS, Long.parseLong(s[1]));
                    case ZK_PENDING_SYNCS:
                        map.put(ZK_PENDING_SYNCS, Long.parseLong(s[1]));
                    case ZK_ZNODE_COUNT:
                        map.put(ZK_ZNODE_COUNT, Long.parseLong(s[1]));
                    case ZK_APPROXIMATE_DATA_SIZE:
                        map.put(ZK_APPROXIMATE_DATA_SIZE, Long.parseLong(s[1]));
                    case ZK_OPEN_FILE_DESCRIPTOR_COUNT:
                        map.put(ZK_OPEN_FILE_DESCRIPTOR_COUNT, Long.parseLong(s[1]));
                    case ZK_MAX_FILE_DESCRIPTOR_COUNT:
                        map.put(ZK_MAX_FILE_DESCRIPTOR_COUNT, Long.parseLong(s[1]));
                    case ZK_WATCH_COUNT:
                        map.put(ZK_WATCH_COUNT, Long.parseLong(s[1]));
                    case ZK_AVG_LATENCY:
                        map.put(ZK_AVG_LATENCY, Long.parseLong(s[1]));
                    case ZK_MAX_LATENCY:
                        map.put(ZK_MAX_LATENCY, Long.parseLong(s[1]));
                    case ZK_MIN_LATENCY:
                        map.put(ZK_MIN_LATENCY, Long.parseLong(s[1]));
                    case ZK_PACKETS_RECEIVED:
                        map.put(ZK_PACKETS_RECEIVED, Long.parseLong(s[1]));
                    case ZK_PACKETS_SENT:
                        map.put(ZK_PACKETS_SENT, Long.parseLong(s[1]));
                    case ZK_NUM_ALIVE_CONNECTIONS:
                        map.put(ZK_NUM_ALIVE_CONNECTIONS, Long.parseLong(s[1]));
                    default:
                        log.info("无需监控指标: {}", s[0]);
                }
            }
            log.info("获取主机 {} Zookeeper 监控指标", host);
            map.entrySet().forEach(entry -> System.out.println(entry.getKey() + " : " + entry.getValue()));
        } finally {
            socket.close();
            if (reader != null) {
                reader.close();
            }
        }
    }
}
