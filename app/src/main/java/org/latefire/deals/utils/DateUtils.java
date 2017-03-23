package org.latefire.deals.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ken on 2/6/2017.
 */

public class DateUtils {
    public static final SimpleDateFormat FORMAT_MONTH_YEAR = new SimpleDateFormat("MMMM yyyy", Locale.US);
    public static final SimpleDateFormat FORMAT_SIMPLEMONTH_YEAR = new SimpleDateFormat("MMM yyyy", Locale.US);
    public static final SimpleDateFormat FORMAT_YEAR_MONTH_DAY = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public static final SimpleDateFormat HOURS_FORMAT = new SimpleDateFormat("kk:mm", Locale.US);
    public static final SimpleDateFormat WEEK_FORMAT = new SimpleDateFormat("ww, yyyy", Locale.US);
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss", Locale.US);
    public static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("MM/dd/yyyy", Locale.US);


    public static final SimpleDateFormat FORMAT_TIME_SLOT_DAY_HEADER = new SimpleDateFormat("EEEE", Locale.US);
    public static final SimpleDateFormat FORMAT_TIME_SLOT_DAY_MONTH_YEAR_HEADER = new SimpleDateFormat("dd MMM yy", Locale.US);

    public static final SimpleDateFormat SINGLE_HOURS_FORMAT = new SimpleDateFormat("hh", Locale.US);
    public static final SimpleDateFormat SIMPLE_HOURS_FORMAT = new SimpleDateFormat("hh:mm", Locale.US);

    public static final SimpleDateFormat TIME_AM_PM_FORMAT = new SimpleDateFormat("hh:mm a", Locale.US);
    public static final SimpleDateFormat TIME_AM_PM_DAY_FORMAT = new SimpleDateFormat("hh:mm a MMM dd yyyy", Locale.US);


    public static final SimpleDateFormat FORMAT_DAY_DAY_MONTH_YEAR = new SimpleDateFormat("EEE dd MMM yy", Locale.US);
    public static final SimpleDateFormat FORMAT_DATE_FULL = new SimpleDateFormat("HH:mm MMM dd yyyy", Locale.US);

    private static SimpleDateFormat DAYVIEW_HEADER_DATE_FORMAT1 = new SimpleDateFormat("EEEE");
    private static SimpleDateFormat DAYVIEW_HEADER_DATE_FORMAT2 = new SimpleDateFormat("dd");

    private static SimpleDateFormat ORGANIZER_NOTIFICATION_TIME_FORMAT = new SimpleDateFormat("MMMM dd yyyy", Locale.US);

    private static SimpleDateFormat FORMAT_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
    private static SimpleDateFormat FORMAT_DATE_TIME_FULL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    private static SimpleDateFormat FORMAT_DATE_TIME_2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    public static SimpleDateFormat FORMAT_DATE_TIME_3 = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00", Locale.US);

    public static final SimpleDateFormat FORMAT_DAY_MONTH_YEAR = new SimpleDateFormat("dd MMM yyyy", Locale.US);
    public static final SimpleDateFormat FORMAT_YYYYMMDD = new SimpleDateFormat("yyyyMMdd", Locale.US);
    public static final SimpleDateFormat FORMAT_TIME_TWITTER = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.US);

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static boolean equals(Date a, Date b) {
        return FORMAT_YEAR_MONTH_DAY.format(a).equals(FORMAT_YEAR_MONTH_DAY.format(b));
    }

    public static boolean isSameDate(Date a, Date b) {
        return FORMAT_DATE_TIME.format(a).equals(FORMAT_DATE_TIME.format(b));
    }

    public static boolean isSameHour(Date a, Date b) {
        return SINGLE_HOURS_FORMAT.format(a).equals(SINGLE_HOURS_FORMAT.format(b));
    }

    public static boolean isMoreThanOneDay(Date a, Date b) {
        Calendar calendarA = Calendar.getInstance();
        calendarA.setTime(a);
        calendarA.add(Calendar.DAY_OF_MONTH, 1);
        return equals(calendarA.getTime(), b);
    }

    public static int dayOffset(Date a, Date b) {
        Calendar calendarA = Calendar.getInstance();
        Calendar calendarB = Calendar.getInstance();
        calendarA.setTime(a);
        calendarB.setTime(b);
        resetCalendar(calendarA);
        resetCalendar(calendarB);
        return (int) ((calendarB.getTimeInMillis() - calendarA.getTimeInMillis()) / (1000 * 3600 * 24));
    }

    public static boolean isSoonerTime(Date a, Date b) {
        Calendar calendarA = Calendar.getInstance();
        Calendar calendarB = Calendar.getInstance();
        calendarA.setTime(a);
        calendarB.setTime(b);
        calendarA.set(Calendar.SECOND, 0);
        calendarB.set(Calendar.SECOND, 0);
        calendarA.set(Calendar.MILLISECOND, 0);
        calendarB.set(Calendar.MILLISECOND, 0);
        return calendarA.getTimeInMillis() < calendarB.getTimeInMillis();
    }


    public static boolean isToday(Calendar calendar) {
        return equals(Calendar.getInstance().getTime(), calendar.getTime());
    }

    public static boolean isYesterday(Calendar calendar) {
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return equals(Calendar.getInstance().getTime(), calendar.getTime());
    }

    public static boolean isTomorrow(Calendar calendar) {
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return equals(Calendar.getInstance().getTime(), calendar.getTime());
    }

    public static void resetCalendar(Calendar calendar) {
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
    }

    public static String parseDayDayMonthYearFormat(Calendar calendar) {
        Date date = calendar.getTime();
        return FORMAT_DAY_DAY_MONTH_YEAR.format(date);
    }

    public static String getHeaderForOrganizerList(Date date) {
        return getHeaderForDayView(date) + " " + FORMAT_SIMPLEMONTH_YEAR.format(date);
    }

    public static String getTimeForOrganizerNotification(Date dateStart, Date dateEnd) {
        return ORGANIZER_NOTIFICATION_TIME_FORMAT.format(dateStart) + ", " + SIMPLE_HOURS_FORMAT.format(dateStart) + " - " + SIMPLE_HOURS_FORMAT.format(dateEnd);
    }

    public static String getTimeForOrganizerNotificationTask(Date dateStart) {
        return ORGANIZER_NOTIFICATION_TIME_FORMAT.format(dateStart) + ", " + SIMPLE_HOURS_FORMAT.format(dateStart);
    }

    public static String getHeaderForDayView(Date date) {
        return DAYVIEW_HEADER_DATE_FORMAT1.format(date) + " " + getDayOfMonthSuffix(date);
    }

    public static SimpleDateFormat getOrganizerNotificationTimeFormat() {
        return ORGANIZER_NOTIFICATION_TIME_FORMAT;
    }

    public static String getDayOfMonthSuffix(Date date) {
        int n = Integer.valueOf(DAYVIEW_HEADER_DATE_FORMAT2.format(date));
        if (n >= 11 && n <= 13) {
            return n + "th";
        }
        switch (n % 10) {
            case 1:
                return n + "st";
            case 2:
                return n + "nd";
            case 3:
                return n + "rd";
            default:
                return n + "th";
        }

    }

    public static String parseHourFromCalendar(Calendar calendar) {
        Date date = calendar.getTime();
        return HOURS_FORMAT.format(date);
    }

    public static SimpleDateFormat getFormatDateTime() {
        return FORMAT_DATE_TIME;
    }

    public static SimpleDateFormat getFormatDateTime2() {
        return FORMAT_DATE_TIME_2;
    }

    public static SimpleDateFormat getFormatDateTime3() {
        return FORMAT_DATE_TIME_3;
    }

    public static String getDateTime(long millisecond) {
        return FORMAT_DATE_TIME_FULL.format(new Date(millisecond));
    }

    public static String formatDayMonthYear(Date date) {
        return FORMAT_DAY_MONTH_YEAR.format(date);
    }

    public static String formatYYYYMMDD(Date date) {
        return FORMAT_YYYYMMDD.format(date);
    }

    public static String formatDayMonthYear(long millisecond) {
        return FORMAT_DAY_MONTH_YEAR.format(new Date(millisecond));
    }

    public static String formatDayMonthYear(String formatDateTime) {
        try {
            return FORMAT_DAY_MONTH_YEAR.format(FORMAT_DATE_TIME_2.parse(formatDateTime));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDateTime(Date date) {
        return FORMAT_DATE_TIME.format(date);
    }


    public static String getTime(String time) {
        try {
            Date dateTime = FORMAT_DATE_TIME_2.parse(time);
            return TIME_AM_PM_FORMAT.format(dateTime);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Date getDate(String srtDate) {
        try {
            return getFormatDateTime3().parse(srtDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateTimeFormat2(String dateTime) {
        try {
            Date date = FORMAT_DATE_TIME_2.parse(dateTime);
            return TIME_AM_PM_DAY_FORMAT.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSuggestedArrivalTime(String time) {
        try {
            Date date = TIME_FORMAT.parse(time);
            return TIME_AM_PM_FORMAT.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTimeAgo(long time) {
        long now = System.currentTimeMillis();

        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "Just now";
        }
        if (diff < 2 * MINUTE_MILLIS) {
            return "1m ago";
        }
        if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + "m ago";
        }
        if (diff < 90 * MINUTE_MILLIS) {
            return "1h ago";
        } else/*if (diff < 24 * HOUR_MILLIS)*/ {
            return diff / HOUR_MILLIS + "h ago";
        }
        /*if (diff < 48 * HOUR_MILLIS) {
            return "Yesterday at " + TIME_AM_PM_FORMAT.format(new Date(time));
        }
        if (diff < 24 * 7 * HOUR_MILLIS) {
            return diff / DAY_MILLIS + " days ago at " + TIME_AM_PM_FORMAT.format(new Date(time));
        }
        else {
            return TIME_AM_PM_DAY_FORMAT.format(new Date(time));
        }*/
    }
}
