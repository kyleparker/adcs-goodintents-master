package com.udacity.adcs.app.goodintents.utils;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kyleparker on 11/10/2015.
 */
public class StringUtils {

    public static String getDateString(long date, String pattern) {
        if (date == 0) {
            return "";
        }
        Date d = new Date(date);
        Format format = new SimpleDateFormat(pattern);
        return format.format(d);
    }

    public static String getRelativeTimeAgo(long date) {
        String relativeDate = "";
        relativeDate = getDateDifferenceForDisplay(new Date(date)); //shorter version
        return relativeDate;
    }

    /**
     * Helper method to get relative date
     *
     * @param inputdate
     * @return date difference as a String
     */
    public static String getDateDifferenceForDisplay(Date inputdate) {
        Calendar now = Calendar.getInstance();
        Calendar then = Calendar.getInstance();
        now.setTime(new Date());
        then.setTime(inputdate);
        // Get the represented date in milliseconds
        long nowMs = now.getTimeInMillis();
        long thenMs = then.getTimeInMillis();
        // Calculate difference in milliseconds
        long diff = nowMs - thenMs;
        // Calculate difference in seconds
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);
        if (diffMinutes < 60) {
            return diffMinutes + "m";
        } else if (diffHours < 24) {
            return diffHours + "h";
        } else if (diffDays < 7) {
            return diffDays + "d";
        } else {
            SimpleDateFormat todate = new SimpleDateFormat("MMM dd",
                    Locale.ENGLISH);
            return todate.format(inputdate);
        }
    }
}
