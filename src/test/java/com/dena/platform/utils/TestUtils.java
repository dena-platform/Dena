package com.dena.platform.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public final class TestUtils {
    public static LocalDateTime truncateTimestampToMinute(String timeStamp) {
        Date dateStamp = new Date(Long.valueOf(timeStamp));
        LocalDateTime dateTime = dateStamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return dateTime.truncatedTo(ChronoUnit.MINUTES);
    }

    public static LocalDateTime truncateTimestampToMinute(LocalDateTime timeStamp) {
        return timeStamp.truncatedTo(ChronoUnit.MINUTES);
    }


}
