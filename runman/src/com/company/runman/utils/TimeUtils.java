package com.company.runman.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Administrator on 2015/5/22.
 */
public class TimeUtils {
    public static final String YYYY_MM_DD_FORMAT = "yyyy-MM-dd";
    public static final long DEFAULT_DATE = -5364691200000L;
    public static final String DEFAULTFORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TimeSplit = ":";
    public static final String DateSplit = "-";
    /**
     * 12:23 =>12
     * @param str
     * @return
     */
    static public int  getMinuteByTimeStr(String str){
        if(str==null)return 0;
        try{
            return  Integer.valueOf(str.split(TimeUtils.TimeSplit)[1]).intValue();
        }catch (Exception e){
            TraceUtil.traceLog("getHourByTimeStr,para="+str);
        }

        return 0;
    }
    /**
     * 12:23 =>23
     * @param str
     * @return
     */
    static public int  getHourByTimeStr(String str){
        if(str==null)return 0;
        try{
            return Integer.valueOf(str.split(TimeUtils.TimeSplit)[0]).intValue();
        }catch (Exception e){
            TraceUtil.traceLog("getHourByTimeStr,para="+str);
        }

        return 0;
    }

    /**
     *2015-01-02 =>2015
     * @param str
     * @return
     */
    static public int  getYearByDateStr(String str){
        if(str==null)return 0;
        try{
           return  Integer.valueOf(str.split(TimeUtils.DateSplit)[0]).intValue();
        }catch (Exception e){
            TraceUtil.traceLog("getYearByDateStr,para="+str);
        }

        return 0;
    }


    /**
     *2015-01-02 =>1
     * @param str
     * @return
     */
    static public int  getMonthByDateStr(String str){
        if(str==null)return 0;
        try{
            return Integer.valueOf(str.split(TimeUtils.DateSplit)[1]).intValue();
        }catch (Exception e){
            TraceUtil.traceLog("getMonthByDateStr,para="+str);
        }

        return 0;
    }
    /**
     *2015-01-02 =>2
     * @param str
     * @return
     */
    static public int  getDayByDateStr(String str){
        if(str==null)return 0;
        try{
            return Integer.valueOf(str.split(TimeUtils.DateSplit)[2]).intValue();
        }catch (Exception e){
            TraceUtil.traceLog("getDayByDateStr,para="+str);
        }

        return 0;
    }

    /**
     *
     * @param dateStr 2015-01-02
     * @param timeStr 12:30
     * @return  2015-01-02 12:30:00
     */
    static public String  getTimestampStr(String dateStr,String timeStr){
        if(timeStr==null)timeStr="00:00";
        try{
          return dateStr+" "+timeStr+":00";
        }catch (Exception e){
            TraceUtil.traceLog("getDayByDateStr,dateStr="+dateStr+",timeStr="+timeStr);
        }

        return null;
    }

    /**
     *
     * @param dateStr 2015-01-02
     * @param timeStr 12:30
     * @return  Timestamp
     */
    static public Timestamp  getTimestamp(String dateStr,String timeStr){
        if(timeStr==null)timeStr="00:00";
        try{
            String tmp= dateStr+" "+timeStr+":00";
            return string2Timestamp(DEFAULTFORMAT,tmp);
        }catch (Exception e){
            TraceUtil.traceLog("getDayByDateStr,dateStr="+dateStr+",timeStr="+timeStr);
        }

        return null;
    }
    /**
     * cur date()=>12:23
     * @return
     */
    public static String getCurrentHourAndMinute(){
        Calendar currentDate = getCurrentDate();
        String str=currentDate.get(Calendar.HOUR_OF_DAY)+":"+currentDate.get(Calendar.MINUTE);
        return str;
    }
    /**
     * cur date()=>12:23
     * @return
     */
    public static String getHourAndMinuteByDate(Date d){
        if(d==null)return "";
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(d);
        String hour=currentDate.get(Calendar.HOUR_OF_DAY)+"";
        if(hour.length()==1){
            hour="0"+hour;
        }
        String minute=currentDate.get(Calendar.MINUTE)+"";
        if(minute.length()==1){
            minute="0"+minute;
        }

        String str=currentDate.get(Calendar.HOUR_OF_DAY)+":"+currentDate.get(Calendar.MINUTE);
        return str;
    }
    public static Calendar date2Calendar(Date date)
    {
        Calendar cal = null;
        if(date != null)
        {
            cal = GregorianCalendar.getInstance();
            cal.setTime(date);
        }
        return cal;
    }

    public static Calendar timestamp2Calendar(Timestamp timestamp)
    {
        Calendar cal = null;
        if(timestamp != null)
        {
            cal = GregorianCalendar.getInstance();
            cal.setTime(timestamp);
        }
        return cal;
    }

    public static final Timestamp getDefaultTimestamp()
    {
        return new Timestamp(-5364691200000L);
    }

    public static Calendar getCurrentDate()
    {
        return Calendar.getInstance();
    }

    public static Timestamp getCurrentTimestamp()
    {
        long time = System.currentTimeMillis();
        return new Timestamp(time);
    }

    public static Timestamp getCurrentTimestamp(String format)
    {
        return new Timestamp(new Date().getTime());
    }

    /**
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentTimeStr()
    {
        return new SimpleDateFormat(DEFAULTFORMAT).format( new Date());
    }
    /**
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateStr()
    {
        return new SimpleDateFormat(YYYY_MM_DD_FORMAT).format( new Date());
    }

    public static boolean isDefaultTimestamp(Timestamp time)
    {
        return time.getTime() == -5364691200000L;
    }

    public static boolean isDefaultTimestamp(Object time)
    {
        if(time instanceof Timestamp)
        {
            Timestamp timeValue = (Timestamp)time;
            return timeValue.getTime() == -5364691200000L;
        } else
        {
            return false;
        }
    }

    public static final Timestamp calendar2Timestamp(Calendar cal)
    {
        Date date = null;
        date = cal.getTime();
        return new Timestamp(date.getTime());
    }

    public static String timestamp2String(String format, Timestamp time)
    {
        if(getDefaultTimestamp().equals(time))
            return "";
        if(format == null)
            format = DEFAULTFORMAT;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        if(time == null)
            return null;
        else
            return formatter.format(time);
    }

    public static final Timestamp string2Timestamp(String format, String time)
    {
        if(format == null)
            format = DEFAULTFORMAT;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try
        {
            date = simpleDateFormat.parse(time);
        }
        catch(ParseException e)
        {
            return null;
        }
        return new Timestamp(date.getTime());
    }

    public static final Calendar string2Calendar(String format, String cal)
    {
        if(format == null)
            format = DEFAULTFORMAT;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try
        {
            date = simpleDateFormat.parse(cal);
        }
        catch(ParseException e)
        {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static final int getMonthDays(int year, int month)
    {
        switch(month)
        {
            case 1: // '\001'
                return 31;

            case 2: // '\002'
                return year % 4 != 0 || year % 100 == 0 && year % 400 != 0 ? 28 : 29;

            case 3: // '\003'
                return 31;

            case 4: // '\004'
                return 30;

            case 5: // '\005'
                return 31;

            case 6: // '\006'
                return 30;

            case 7: // '\007'
                return 31;

            case 8: // '\b'
                return 31;

            case 9: // '\t'
                return 30;

            case 10: // '\n'
                return 31;

            case 11: // '\013'
                return 30;

            case 12: // '\f'
                return 31;
        }
        return 0;
    }

    public static int getDaysDiff(String sdate1, String sdate2, String format, TimeZone tz)
    {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date1 = null;
        Date date2 = null;
        try
        {
            date1 = df.parse(sdate1);
            date2 = df.parse(sdate2);
        }
        catch(ParseException pe)
        {
            return -1;
        }
        Calendar cal1 = null;
        Calendar cal2 = null;
        if(tz == null)
        {
            cal1 = Calendar.getInstance();
            cal2 = Calendar.getInstance();
        } else
        {
            cal1 = Calendar.getInstance(tz);
            cal2 = Calendar.getInstance(tz);
        }
        cal1.setTime(date1);
        long ldate1 = date1.getTime() + (long)cal1.get(15) + (long)cal1.get(16);
        cal2.setTime(date2);
        long ldate2 = date2.getTime() + (long)cal2.get(15) + (long)cal2.get(16);
        int hr1 = (int)(ldate1 / 3600000L);
        int hr2 = (int)(ldate2 / 3600000L);
        int days1 = hr1 / 24;
        int days2 = hr2 / 24;
        int dateDiff = days2 - days1;
        return dateDiff;
    }

    public static Calendar getWeek(int num)
    {
        Calendar date = Calendar.getInstance();
        int weekOfYear = date.get(3);
        weekOfYear += num;
        date.set(3, weekOfYear);
        return date;
    }

    public static Calendar getBeforeWeek(int num)
    {
        Calendar date = Calendar.getInstance();
        int weekOfYear = date.get(3);
        weekOfYear -= num;
        date.set(3, weekOfYear);
        return date;
    }

    public static Calendar getMonth(int num)
    {
        Calendar date = Calendar.getInstance();
        int monthOfYear = date.get(2);
        monthOfYear += num;
        date.set(2, monthOfYear);
        return date;
    }

    public static Calendar getBeforeMonth(int num)
    {
        Calendar date = Calendar.getInstance();
        int monthOfYear = date.get(2);
        monthOfYear += num;
        date.set(2, monthOfYear);
        return date;
    }

    public static Calendar getDay(int num)
    {
        Calendar date = Calendar.getInstance();
        int dayOfYear = date.get(5);
        dayOfYear += num;
        date.set(5, dayOfYear);
        return date;
    }

    public static Timestamp date2TimestampStart(Date date)
    {
        return new Timestamp(date.getTime());
    }

    public static String getDateString(Date date)
    {
        if(date==null)return "";
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_FORMAT);
        return format.format(date);
    }




}
