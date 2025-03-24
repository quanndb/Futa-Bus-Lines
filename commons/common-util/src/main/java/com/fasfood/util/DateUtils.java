package com.fasfood.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;

public final class DateUtils {
    public static final long MS = 1L;
    public static final long SECOND_MS = 1000L;
    public static final long MINUTE_MS = 60000L;
    public static final long HOUR_MS = 3600000L;
    public static final long DAY_MS = 86400000L;
    public static final String NORM_DATE_PATTERN = "yyyy-MM-dd";
    public static final String NORM_2_DATE_PATTERN = "yyyy/MM/dd";
    public static final String NORM_3_DATE_PATTERN = "dd/MM/yyyy";
    public static final String NORM_4_DATE_PATTERN = "ddMMyyyy";
    public static final String NORM_TIME_PATTERN = "HH:mm:ss";
    public static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String NORM_2_DATETIME_PATTERN = "HH:mm dd/MM/yyyy ";
    public static final String NORM_3_DATETIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
    public static final String HTTP_DATETIME_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
    public static final String ERROR = " error!";
    public static final String PARSE = "Parse ";
    public static final String WITH_FORMAT = " with format ";
    private static final Logger log = LoggerFactory.getLogger(DateUtils.class);
    public static final String MONTH_YEAR_DATE_PATTERN = "MM/yyyy";

    private DateUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String now() {
        return formatDateTime(new Date());
    }

    public static String formatDateTime(Date date) {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
    }

    public static String today() {
        return formatDate(new Date());
    }

    public static String formatDate(Date date) {
        return (new SimpleDateFormat("yyyy-MM-dd")).format(date);
    }

    public static String format(Date date, String format) {
        return (new SimpleDateFormat(format)).format(date);
    }

    public static String format(Date date, String format, TimeZone timeZone) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        sf.setTimeZone(timeZone);
        return sf.format(date);
    }

    public static String format(LocalDate localDate, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDate.format(formatter);
    }

    public static String formatLocalDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return localDate.format(formatter);
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        return localDateTime.format(formatter);
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }

    public static String formatInstant(Instant instant, ZoneId zoneId, String pattern) {
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(zonedDateTime);
    }

    public static LocalDate parseToLocalDate(String dateText) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateText, formatter);
    }

    public static Optional<LocalDate> tryParseToLocalDate(String dateText) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return Optional.of(LocalDate.parse(dateText, formatter));
        } catch (Exception var2) {
            return Optional.empty();
        }
    }

    public static String formatHttpDate(Date date) {
        return (new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)).format(date);
    }

    public static Date parsee(String dateString, String format) {
        try {
            return (new SimpleDateFormat(format)).parse(dateString);
        } catch (ParseException e) {
            log.error("Parse " + dateString + " with format " + format + " error!", e);
            return null;
        }
    }

    public static LocalDate firstOfPeriod(ReportingPeriodType type, Integer yearOfReport) {
        if (ReportingPeriodType.FIRST_QUARTER.equals(type)) {
            return firstOfMonth(yearOfReport, Month.JANUARY);
        } else if (ReportingPeriodType.SECOND_QUARTER.equals(type)) {
            return firstOfMonth(yearOfReport, Month.APRIL);
        } else if (ReportingPeriodType.THIRD_QUARTER.equals(type)) {
            return firstOfMonth(yearOfReport, Month.JULY);
        } else if (ReportingPeriodType.FOUR_QUARTER.equals(type)) {
            return firstOfMonth(yearOfReport, Month.OCTOBER);
        } else if (ReportingPeriodType.FIRST_SIX_MONTH.equals(type)) {
            return firstOfMonth(yearOfReport, Month.JANUARY);
        } else {
            return ReportingPeriodType.LAST_SIX_MONTH.equals(type) ? firstOfMonth(yearOfReport, Month.JULY) : firstOfYear(yearOfReport);
        }
    }

    public static LocalDate firstOfMonth(Integer year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate firstOfYear(Integer year) {
        return LocalDate.of(year, Month.JANUARY, 1);
    }

    public static LocalDate lastOfPeriod(ReportingPeriodType type, Integer yearOfReport) {
        if (ReportingPeriodType.FIRST_QUARTER.equals(type)) {
            return lastOfMonth(yearOfReport, Month.MARCH);
        } else if (ReportingPeriodType.SECOND_QUARTER.equals(type)) {
            return lastOfMonth(yearOfReport, Month.JUNE);
        } else if (ReportingPeriodType.THIRD_QUARTER.equals(type)) {
            return lastOfMonth(yearOfReport, Month.SEPTEMBER);
        } else if (ReportingPeriodType.FOUR_QUARTER.equals(type)) {
            return lastOfMonth(yearOfReport, Month.DECEMBER);
        } else if (ReportingPeriodType.FIRST_SIX_MONTH.equals(type)) {
            return lastOfMonth(yearOfReport, Month.JUNE);
        } else {
            return ReportingPeriodType.LAST_SIX_MONTH.equals(type) ? lastOfMonth(yearOfReport, Month.DECEMBER) : lastOfYear(yearOfReport);
        }
    }

    public static LocalDate lastOfMonth(Integer year, Month month) {
        return LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDate lastOfYear(Integer year) {
        return LocalDate.of(year, Month.DECEMBER, 31);
    }

    public static List<Month> getMonthByPeriod(ReportingPeriodType type) {
        if (ReportingPeriodType.FIRST_QUARTER.equals(type)) {
            return List.of(Month.JANUARY, Month.FEBRUARY, Month.MARCH);
        } else if (ReportingPeriodType.SECOND_QUARTER.equals(type)) {
            return List.of(Month.APRIL, Month.MAY, Month.JUNE);
        } else if (ReportingPeriodType.THIRD_QUARTER.equals(type)) {
            return List.of(Month.JULY, Month.AUGUST, Month.SEPTEMBER);
        } else if (ReportingPeriodType.FOUR_QUARTER.equals(type)) {
            return List.of(Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER);
        } else if (ReportingPeriodType.FIRST_SIX_MONTH.equals(type)) {
            return List.of(Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY, Month.JUNE);
        } else {
            return ReportingPeriodType.LAST_SIX_MONTH.equals(type) ? List.of(Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER) : List.of(Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY, Month.JUNE, Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER);
        }
    }

    public static Date yesterday() {
        return offsiteDate(new Date(), 6, -1);
    }

    public static Date offsiteDate(Date date, int calendarField, int offsite) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(calendarField, offsite);
        return cal.getTime();
    }

    public static Date lastWeek() {
        return offsiteDate(new Date(), 3, -1);
    }

    public static Date lastMouth() {
        return offsiteDate(new Date(), 2, -1);
    }

    public static long diff(Date subtrahend, Date minuend, long diffField) {
        long diff = minuend.getTime() - subtrahend.getTime();
        return diff / diffField;
    }

    public static long spendNt(long preTime) {
        return System.nanoTime() - preTime;
    }

    public static long spendMs(long preTime) {
        return System.currentTimeMillis() - preTime;
    }

    public static Instant parseStartOfDay(String dateStr) {
        Date date = parsee(dateStr);
        if (date != null) {
            date = atStartOfDay(date);
            return dateToLocalDateTime(date).toInstant(ZoneOffset.ofHours(7));
        } else {
            return null;
        }
    }

    public static Date parsee(String dateStr) {
        int length = dateStr.length();

        try {
            if (length == "yyyy-MM-dd HH:mm:ss".length()) {
                return parseDateTime(dateStr);
            }

            if (length == "yyyy-MM-dd".length()) {
                return parseDate(dateStr);
            }

            if (length == "HH:mm:ss".length()) {
                return parseTime(dateStr);
            }
        } catch (Exception e) {
            log.error("Parse " + dateStr + " with format normal error!", e);
        }

        return null;
    }

    public static Date atStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date parseDateTime(String dateString) {
        try {
            return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(dateString);
        } catch (ParseException e) {
            log.error("Parse " + dateString + " with format " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).toPattern() + " error!", e);

            try {
                return (new SimpleDateFormat("HH:mm dd/MM/yyyy ")).parse(dateString);
            } catch (ParseException ex) {
                log.error("Parse " + dateString + " with format " + (new SimpleDateFormat("HH:mm dd/MM/yyyy ")).toPattern() + " error!", ex);
                return null;
            }
        }
    }

    public static Date parseDate(String dateString) {
        try {
            return (new SimpleDateFormat("yyyy-MM-dd")).parse(dateString);
        } catch (ParseException e) {
            log.error("Parse " + dateString + " with format yyyy-MM-dd oryyyy/MM/dd error!", e);

            try {
                return (new SimpleDateFormat("yyyy/MM/dd")).parse(dateString);
            } catch (ParseException ex) {
                log.error("Parse " + dateString + " with format yyyy/MM/dd error!", ex);
                return null;
            }
        }
    }

    public static Date parseTime(String timeString) {
        try {
            return (new SimpleDateFormat("HH:mm:ss")).parse(timeString);
        } catch (ParseException e) {
            log.error("Parse " + timeString + " with format HH:mm:ss error!", e);
            return null;
        }
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Instant parseEndOfDay(String dateStr) {
        Date date = parsee(dateStr);
        if (date != null) {
            date = atEndOfDay(date);
            return dateToLocalDateTime(date).toInstant(ZoneOffset.ofHours(7));
        } else {
            return null;
        }
    }

    public static Date atEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    public static Instant getTimeStart(String dateText) {
        LocalDateTime date = LocalDateTime.parse(dateText + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return date.toInstant(ZoneOffset.ofHours(7));
    }

    public static Instant getTimeFinish(String dateText) {
        LocalDateTime date = LocalDateTime.parse(dateText + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return date.toInstant(ZoneOffset.ofHours(7));
    }

    public static Instant parseDateTimeToInstant(String dateTimeString, ZoneId zoneId, String pattern) {
        if (Objects.isNull(dateTimeString)) {
            return null;
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

            try {
                String compactedStr = dateTimeString.replaceAll("\\s+", " ");
                LocalDateTime localDateTime = LocalDateTime.parse(compactedStr, formatter);
                return localDateTime.atZone(zoneId).toInstant();
            } catch (Exception e) {
                System.err.println("Error parsing date: " + e.getMessage());
                return null;
            }
        }
    }

    public static Date getFirstDayOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, 1);
        return atStartOfDay(calendar.getTime());
    }

    public static Date getFirstDayOfCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        return atStartOfDay(calendar.getTime());
    }

    public static Integer getDayCount(String start, String end) {
        int diff = -1;

        try {
            Date dateStart = parseDate(start);
            Date dateEnd = parseDate(end);
            if (dateEnd == null || dateStart == null) {
                return 0;
            }

            diff = Math.toIntExact(Math.round((double) (dateEnd.getTime() - dateStart.getTime()) / (double) 8.64E7F));
        } catch (Exception ignored) {
        }

        return diff + 1;
    }

    public static String formatLocalDate(LocalDate localDate, String pattern) {
        if (Objects.isNull(localDate)) {
            return "";
        } else {
            try {
                return format(localDate, pattern);
            } catch (Exception var3) {
                return "";
            }
        }
    }

    public static long getWeekIndexOfDate(LocalDate date) {
        long weekIndexOfDate = 1L;
        LocalDate firstMonday = date.with(TemporalAdjusters.firstDayOfMonth()).with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        if (ChronoUnit.DAYS.between(firstMonday, date) < 0L) {
            return weekIndexOfDate;
        } else {
            LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
            int weekIndexOfFirstMonday = Objects.equals(firstDay, firstMonday) ? 1 : 2;
            return ChronoUnit.DAYS.between(firstMonday, date) / 7L + (long) weekIndexOfFirstMonday;
        }
    }

    public static List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        if (!Objects.isNull(startDate) && !Objects.isNull(endDate)) {
            if (startDate.isAfter(endDate)) {
                LocalDate tempDate = startDate;
                startDate = endDate;
                endDate = tempDate;
            }

            List<LocalDate> dateList = new ArrayList<>();

            for (LocalDate currentDate = startDate.plusDays(0L); !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1L)) {
                dateList.add(currentDate);
            }

            return dateList;
        } else {
            return new ArrayList<>();
        }
    }

    public static List<Month> getMonthsBetween(LocalDate startDate, LocalDate endDate) {
        if (!Objects.isNull(startDate) && !Objects.isNull(endDate)) {
            if (startDate.isAfter(endDate)) {
                LocalDate tempDate = startDate;
                startDate = endDate;
                endDate = tempDate;
            }

            List<Month> monthList = new ArrayList<>();

            for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusMonths(1L)) {
                monthList.add(currentDate.getMonth());
            }

            return monthList;
        } else {
            return new ArrayList<>();
        }
    }

    public static List<YearMonth> getYearMonthsBetween(LocalDate startDate, LocalDate endDate) {
        if (!Objects.isNull(startDate) && !Objects.isNull(endDate)) {
            if (startDate.isAfter(endDate)) {
                LocalDate tempDate = startDate;
                startDate = endDate;
                endDate = tempDate;
            }

            List<YearMonth> monthList = new ArrayList();

            for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusMonths(1L)) {
                monthList.add(YearMonth.of(currentDate.getYear(), currentDate.getMonth()));
            }

            return monthList;
        } else {
            return new ArrayList();
        }
    }

    public static double calculateDurationInHour(Instant start, Instant end) {
        if (!Objects.isNull(start) && !Objects.isNull(end)) {
            start = start.truncatedTo(ChronoUnit.MINUTES);
            end = end.truncatedTo(ChronoUnit.MINUTES);
            return (double) Duration.between(start, end).toMillis() / (double) 3600000.0F;
        } else {
            return (double) 0.0F;
        }
    }

    public static double calculateDurationInMinute(Instant start, Instant end) {
        return (double) Duration.between(start, end).toMillis() / (double) 60000.0F;
    }

    public static double calculateDurationInDay(Instant start, Instant end) {
        return (double) Duration.between(start, end).toMillis() / (double) 8.64E7F;
    }
}
