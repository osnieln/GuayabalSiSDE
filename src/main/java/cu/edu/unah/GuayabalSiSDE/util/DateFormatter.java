package cu.edu.unah.GuayabalSiSDE.util;

import jakarta.validation.constraints.NotNull;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    private static final String DATE_PATTERN  = "yyyyMMdd";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN).withZone(ZoneOffset.UTC);

    public static Date format(@NotNull String date){
        LocalDate localDate = LocalDate.parse(date, FORMATTER);
        return Date.valueOf(localDate);
    }

    public static String format(@NotNull Date date){
        LocalDate localDate = date.toLocalDate();
        return localDate.format(FORMATTER);
    }
}
