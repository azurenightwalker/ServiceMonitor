package com.androidproductions.generic.lib.dates;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateFormatter {

    public static String AsCuiDate(Calendar cal)
    {
        return AsCuiDate(cal.getTime());
    }

    public static String AsCuiDate(Date date)
    {
        return new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(date);
    }

    public static String AsCuiTime(Calendar cal)
    {
        return AsCuiTime(cal.getTime());
    }

    public static String AsCuiTime(Date date)
    {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
    }

    public static String AsCuiDateTime(Calendar cal)
    {
        return AsCuiDateTime(cal.getTime());
    }

    public static String AsCuiDateTime(Date date)
    {
        return new SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.getDefault()).format(date);
    }

}
