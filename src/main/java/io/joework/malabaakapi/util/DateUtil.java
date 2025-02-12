package io.joework.malabaakapi.util;

import java.time.LocalDateTime;

public class DateUtil {

    public static boolean isExpired(LocalDateTime date) {
        return date.isBefore(LocalDateTime.now());
    }
}
