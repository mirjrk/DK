package com.zesium.android.betting.model.util;

import com.zesium.android.betting.utils.AppLogger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Helper class that contains all common methods for date and time.
 * <p>
 * Created by Ivan Panic_2 on 10/18/2016.
 */
public class DateAndTimeHelper {
    private static final String RECEIVED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String RECEIVED_DATE_FORMAT_BEST_OFFER = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String TAG = DateAndTimeHelper.class.getSimpleName();
    private static final SimpleDateFormat localDateFormat = new SimpleDateFormat(RECEIVED_DATE_FORMAT, Locale.getDefault());
    private static final SimpleDateFormat receivedFormat = new SimpleDateFormat(RECEIVED_DATE_FORMAT, Locale.getDefault());
    private static final SimpleDateFormat receivedFormatBestOffer = new SimpleDateFormat(RECEIVED_DATE_FORMAT_BEST_OFFER, Locale.getDefault());

    private static final String dateFormat = "dd.MM.yyyy";
    private static final String timeFormat = "HH:mm";
    public static final SimpleDateFormat DATE_VALUE_FORMAT = new SimpleDateFormat(dateFormat, Locale.getDefault());
    public static final SimpleDateFormat TIME_VALUE_FORMAT = new SimpleDateFormat(timeFormat, Locale.getDefault());

    public static final String REGISTRATION_DATE = "dd-MMM-yyyy";
    public static final String REGISTRATION_DATE_FOR_SERVER = "MM/dd/yyyy HH:mm:ss";


    public static String changeDateFormatWithTime(String time) {
        String format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        String formatOutput = "dd.MM.yyyy. HH:mm";
        SimpleDateFormat sdFormater = new SimpleDateFormat(format, Locale.getDefault());
        sdFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date inputDt = null;
        try {
            inputDt = sdFormater.parse(time);
        } catch (ParseException e) {
            AppLogger.error(TAG, "Exception occurred with message: ", e);
        }
        SimpleDateFormat formatter2 = new SimpleDateFormat(formatOutput, Locale.getDefault());
        return formatter2.format(inputDt);
    }

    public static SimpleDateFormat getReceivedFormat() {
        receivedFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return receivedFormat;
    }

    public static SimpleDateFormat getReceivedFormatBestOffer() {
        receivedFormatBestOffer.setTimeZone(TimeZone.getTimeZone("UTC"));
        return receivedFormatBestOffer;
    }

    public static SimpleDateFormat getLocalDateFormat() {
        localDateFormat.setTimeZone(TimeZone.getDefault());
        return localDateFormat;
    }
}
