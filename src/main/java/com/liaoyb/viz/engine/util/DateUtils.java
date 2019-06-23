package com.liaoyb.viz.engine.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 日期工具
 *
 * @author liaoyb
 */
public class DateUtils {
    public static Date toDate(Timestamp timestamp) {
        if (null == timestamp) {
            return null;
        }
        return new Date(timestamp.getTime());
    }


    public static LocalDateTime toDateTime(Date date) {
        if (null == date) {
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        return instant.atZone(zoneId).toLocalDateTime();
    }
}
