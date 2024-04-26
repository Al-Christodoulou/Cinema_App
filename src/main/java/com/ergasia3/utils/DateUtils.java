package com.ergasia3.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

final public class DateUtils {
    final static private String HTMLDatePattern = "yyyy-MM-dd";
    final static private String HTMLTimePattern = "HH:mm";

    static private Date innerConvertDateTime(String in, String pattern) {
        // a SimpleDateFormat object can convert a date String into a Date object
        final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.parse(in);
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    static public Date convToDateFromHTMLParam(String in) {
        return innerConvertDateTime(in, HTMLDatePattern);
    }

    static public Date convToTimeFromHTMLParam(String in) {
        return innerConvertDateTime(in, HTMLTimePattern);
    }

    static public String convToHTMLDateFromDate(Date in) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(HTMLDatePattern);
        return dateFormat.format(in);
    }

    static public String convToHTMLTimeFromDate(Date in ) {
        final SimpleDateFormat timeFormat = new SimpleDateFormat(HTMLTimePattern);
        return timeFormat.format(in);
    }

    static public Date combineDateTime(Date date, Date time) {
        // for some reason two hours have to be summed to the scheduled time, so it's correct according
        // to what the user selected
        final long twoHours = 1000 * 60 * 60 * 2;
        return new Date(date.getTime() + time.getTime() + twoHours);
    }
}
