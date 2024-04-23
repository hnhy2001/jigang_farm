package com.example.jingangfarmmanagement.uitl;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public static Long getDate(Long dateTime) {
        return dateTime / 1000000;
    }

    public static Long getCurrenDate() {
        return Long.parseLong(dateFormat.format(new Date()));
    }

    public static Long getCurrenDateTime() {
        return Long.parseLong(dateTimeFormat.format(new Date()));
    }
}
