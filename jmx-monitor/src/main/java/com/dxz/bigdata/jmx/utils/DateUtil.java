package com.dxz.bigdata.jmx.utils;


import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateUtil {
    public static Long getTimestampMil() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }
}
