package com.pr7.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class DateUtil {

    private static final Logger _logger = LogManager.getLogger(DateUtil.class);

    public static Date convertToTimeZone(String str, String format, TimeZone sourceTz, TimeZone targetTz) {
        try {
            DateFormat df = new SimpleDateFormat(format);
            df.setTimeZone(sourceTz);
            Date dateToChange = df.parse(str);

            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(targetTz);
            cal.setTime(dateToChange);
            return cal.getTime();
        } catch (Exception e) {
            _logger.error("error when call convertToTimeZone:: str = " + str + ", format = " + format + ", sourceTz = " + sourceTz.getID() + ", targetTz = " + targetTz.getID());
            return null;
        }
    }

    public static Date parse(String str, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format);
            return df.parse(str);
        } catch (ParseException e) {
            _logger.error("ERROR when parse: " + str + ", format: " + format, e);
        }

        return null;
    }

    public static String format(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static String format(Date date, String format, TimeZone timezone) {
        DateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(timezone);
        return df.format(date);
    }

    public static double minuteDiff(Date source, Date target) {
        Calendar sourceCal = Calendar.getInstance();
        sourceCal.setTime(source);

        Calendar targetCal = Calendar.getInstance();
        targetCal.setTime(target);

        return ((double) sourceCal.getTimeInMillis() - targetCal.getTimeInMillis()) / (60 * 1000);
    }

    public static int dayDiff(Date source, Date target) {
        Calendar sourceCal = Calendar.getInstance();
        sourceCal.setTime(DateUtils.truncate(source, Calendar.DATE));

        Calendar targetCal = Calendar.getInstance();
        targetCal.setTime(DateUtils.truncate(target, Calendar.DATE));
		Long result = (sourceCal.getTimeInMillis() - targetCal.getTimeInMillis()) / (24 * 60 * 60 * 1000);
        return result.intValue();
    }
    
    public static String serverToClientDateString(Date date, String format, String timeZone) {
        int tzOffset = StringUtils.isBlank(timeZone) ? TimeZone.getDefault().getRawOffset() / 60000 : convertTimeZoneOffset(timeZone);
        return serverToClientDateString(date, format, tzOffset);
    }

    public static Date serverToClientDate(Date date,int timeZoneOffset){        
        try {
            return DateUtils.parseDate(serverToClientDateString(date,"yyyyMMdd HH:mm:ss", timeZoneOffset), new String [] {"yyyyMMdd HH:mm:ss"} );
        } catch (Exception ex) {
            _logger.warn("Convert TimeZoneOffset got error " + ex.getMessage());
        }
        return date;
    }
    
    public static String serverToClientDateString(Date date, String format, int timeZoneOffset) {
        String[] tzIds = TimeZone.getAvailableIDs(timeZoneOffset* 60000);
        TimeZone tz = null;
        if (tzIds.length > 0) {
            tz = TimeZone.getTimeZone(tzIds[0]);
        }
        if (tz != null) {
            SimpleDateFormat df = new SimpleDateFormat(format);
            df.setTimeZone(tz);
            return df.format(date);            
        } else {
            _logger.warn("Cant find time Zone with TimeZoneOffset: " + timeZoneOffset);
        }
        return "";
    }

    public static Date clientToServerDate(String clientDate, int timeZoneOffset, String... dateFormats) {
        if(dateFormats == null || dateFormats.length == 0){
            dateFormats = new String[]{"MM/dd/yyyy HH:mm:ss", "dd/MM/yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyyMMdd HH:mm:ss", "ddMMyyyy", "yyyy-MM-dd", "yyyyMMdd", "MM/dd/yyyy"};
        }
        String[] tzIds = TimeZone.getAvailableIDs(timeZoneOffset * 60000);
        TimeZone tz = null;
        Date date = null;
        if (tzIds.length > 0) {
            tz = TimeZone.getTimeZone(tzIds[0]);
        }
        if (tz != null) {
            for (String pt : dateFormats) {
                SimpleDateFormat df = new SimpleDateFormat(pt);
                df.setTimeZone(tz);
                try {
                    date = df.parse(clientDate);
                    break;
                } catch (ParseException ex) {
                }
            }
            if (date != null) {
                date = new Date(date.getTime());
            }
        } else {
            _logger.warn("Cant find time Zone with TimeZoneOffset: " + timeZoneOffset);
        }

        return date;
    }
    public static Date clientToServerDate(String clientDate, String timeZone, String... dateFormats) throws ParseException {
        if(dateFormats == null || dateFormats.length == 0){
            dateFormats = new String[]{"MM/dd/yyyy HH:mm:ss", "dd/MM/yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyyMMdd HH:mm:ss", "ddMMyyyy", "yyyy-MM-dd", "yyyyMMdd", "MM/dd/yyyy"};
        }
        if (StringUtils.isBlank(timeZone)) {
            return DateUtils.parseDate(clientDate, dateFormats);
        }

        int timeZoneOffset = convertTimeZoneOffset(timeZone);
        return clientToServerDate(clientDate, timeZoneOffset, dateFormats);
    }

    public static int convertTimeZoneOffset(String timeZone) throws NumberFormatException {
        int offset = 0;
        timeZone = timeZone.replace("GMT", "").replaceAll("\\s|\\+", "");
        if(StringUtils.isNotBlank(timeZone)){
            String[] timeZoneParts = timeZone.split(":");
            offset = Integer.parseInt(timeZoneParts[0]) * 60;
            if (timeZoneParts.length > 1 && StringUtils.isNotBlank(timeZoneParts[1])) {
                int m = Integer.valueOf(timeZoneParts[1]);
                m = timeZone.startsWith("-") ? -m : m;
                offset += m;
            }
        }
        return offset;
    }
}
