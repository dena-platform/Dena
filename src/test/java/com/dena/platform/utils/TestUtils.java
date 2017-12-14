package com.dena.platform.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public final class TestUtils {

    public static boolean isTimeEqualRegardlessOfMinute(long timestamp1, long timestamp2) {
        LocalDateTime dateTime1 = new Date(timestamp1).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime dateTime2 = new Date(timestamp2).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return dateTime1.truncatedTo(ChronoUnit.MINUTES).equals(dateTime2.truncatedTo(ChronoUnit.MINUTES));

    }


}
