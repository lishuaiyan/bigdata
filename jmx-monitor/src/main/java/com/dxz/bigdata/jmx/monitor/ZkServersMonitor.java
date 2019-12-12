package com.dxz.bigdata.jmx.monitor;

import com.dxz.bigdata.jmx.model.ThreadMonitor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static com.dxz.bigdata.jmx.constant.ItemConstants.*;

@Slf4j
public class ZkServersMonitor implements ThreadMonitor {
    private static final String cmd = "mntr";
    private static final int port = 2181;

    @Override
    public HashMap<String, Object> getMap(Map.Entry<Object, Object> entry, CloseableHttpClient httpClient) throws IOException {
        HashMap<String, Object> map = new HashMap<>(16);
        String host = entry.getValue().toString();
        Socket socket = new Socket(host, port);
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
                String[] s = line.split("\t");
                switch (s[0]) {
                    case ZK_SERVER_STATE:
                        map.put(ZK_SERVER_STATE,s[1]);
                        break;
                    case ZK_FOLLOWERS:
                        map.put(ZK_FOLLOWERS, s[1]);
                        break;
                    case ZK_SYNCED_FOLLOWERS:
                        map.put(ZK_SYNCED_FOLLOWERS, s[1]);
                        break;
                    case ZK_OUTSTANDING_REQUESTS:
                        map.put(ZK_OUTSTANDING_REQUESTS, s[1]);
                        break;
                    case ZK_PENDING_SYNCS:
                        map.put(ZK_PENDING_SYNCS, s[1]);
                        break;
                    case ZK_ZNODE_COUNT:
                        map.put(ZK_ZNODE_COUNT, s[1]);
                        break;
                    case ZK_APPROXIMATE_DATA_SIZE:
                        map.put(ZK_APPROXIMATE_DATA_SIZE, s[1]);
                        break;
                    case ZK_OPEN_FILE_DESCRIPTOR_COUNT:
                        map.put(ZK_OPEN_FILE_DESCRIPTOR_COUNT, s[1]);
                        break;
                    case ZK_MAX_FILE_DESCRIPTOR_COUNT:
                        map.put(ZK_MAX_FILE_DESCRIPTOR_COUNT, s[1]);
                        break;
                    case ZK_WATCH_COUNT:
                        map.put(ZK_WATCH_COUNT, s[1]);
                        break;
                    case ZK_AVG_LATENCY:
                        map.put(ZK_AVG_LATENCY, s[1]);
                        break;
                    case ZK_MAX_LATENCY:
                        map.put(ZK_MAX_LATENCY, s[1]);
                        break;
                    case ZK_MIN_LATENCY:
                        map.put(ZK_MIN_LATENCY, s[1]);
                        break;
                    case ZK_PACKETS_RECEIVED:
                        map.put(ZK_PACKETS_RECEIVED, s[1]);
                        break;
                    case ZK_PACKETS_SENT:
                        map.put(ZK_PACKETS_SENT, s[1]);
                        break;
                    case ZK_NUM_ALIVE_CONNECTIONS:
                        map.put(ZK_NUM_ALIVE_CONNECTIONS, s[1]);
                        break;
                    default:
                        log.info("无需监控指标: {}", s[0]);
                        break;
                }
            }
            log.info("获取主机 {} Zookeeper 监控指标", host);

        } finally {
            socket.close();
            if (reader != null) {
                reader.close();
            }
        }
//        map.forEach((key, value) -> {
//            System.out.println(key + ":" + value);
//        });
        System.out.println(map.size());
        return map;
    }

}
