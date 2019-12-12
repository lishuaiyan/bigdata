package com.dxz.bigdata.jmx.model;

import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public interface ThreadMonitor {
    HashMap<String, Object> getMap(Map.Entry<Object, Object> entry, CloseableHttpClient httpClient) throws IOException;

}
