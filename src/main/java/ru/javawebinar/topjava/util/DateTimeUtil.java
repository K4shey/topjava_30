package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T value, T startValue, T endValue) {
        return (startValue == null || value.compareTo(startValue) >= 0)
                && (endValue == null || value.compareTo(endValue) < 0);
    }

    public static <T extends Comparable<T>> boolean isBetween(T value, T startValue, T endValue) {
        return (startValue == null || value.compareTo(startValue) >= 0)
                && (endValue == null || value.compareTo(endValue) <= 0);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parseLocalDate(String ld) {
        if (ld == null || ld.isEmpty()) {
            return null;
        }
        return LocalDate.parse(ld);
    }

    public static LocalTime parseLocalTime(String lt) {
        if (lt == null || lt.isEmpty()) {
            return null;
        }
        return LocalTime.parse(lt);
    }
}

