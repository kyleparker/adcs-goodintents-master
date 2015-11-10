package com.udacity.adcs.app.goodintents.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

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
}
