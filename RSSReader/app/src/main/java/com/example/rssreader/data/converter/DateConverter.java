package com.example.rssreader.data.converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateConverter {
    public static ZonedDateTime toZonedDateTime(Long epochSecond) {
        return epochSecond == null ? null : ZonedDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneId.systemDefault());
    }

    public static Long toLong(ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : zonedDateTime.toEpochSecond();
    }

    public static ZonedDateTime toZonedDateTime(String zonedDateTime) {
        return zonedDateTime == null ? null : ZonedDateTime.parse(zonedDateTime,
                DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss xxxx"));
    }
}
