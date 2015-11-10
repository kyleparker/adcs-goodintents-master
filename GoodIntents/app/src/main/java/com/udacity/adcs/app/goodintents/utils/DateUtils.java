package com.udacity.adcs.app.goodintents.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by perklun on 11/10/2015.
 */
public class DateUtils {

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = getDateDifferenceForDisplay(new Date(dateMillis)); //shorter version
            //relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
