package com.portoitapoa.faturamentofast.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    /**
     * Converts a {@link LocalDate} to a string with the format "yyyy-MM-dd".
     *
     * @param localDate the date to be formatted
     * @return the string representation of the date
     */
    public static String formatLocalDateToString(final LocalDate localDate) {
        if (localDate == null) return null;

        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDate);
    }
}
