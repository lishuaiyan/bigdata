package com.dxz.bigdata.jmx.old;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
@Slf4j
public class Utils {
    public static JSONArray qryJSonObjectFromJMX(CloseableHttpClient httpClient, String URL) throws IOException {
        JSONArray jsonObject = null;
        ByteArrayOutputStream result = null;
        InputStream inputStream = null;
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet httpGet = new HttpGet(URL.toString());
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
            jsonObject = JSONObject.parseObject(jsonStr).getJSONArray("beans");
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
                log.error("HTTP 连接关闭异常: {}", e.getCause());
            }
        }
        log.info(URL + " 成功获取JMX数据并转换为JSON!");
        return jsonObject;
    }
}
