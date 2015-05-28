package com.company.runman.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/5/28.
 * 业务数据转成用户展示数据
 */
public class VOUtils {
    /**
     * 避免显示有null的情况。
     * @param o
     * @return
     */
    static public String objectToString(Object o){
        if(o==null)return "";
        return o+"";
    }

    static public String sexToString(Integer o){
        if(Integer.valueOf(1).equals(o))return "女";
        return "男";
    }

    static public String birthToString(Date o){
        if(o==null)return "";
        Calendar cal= Calendar.getInstance();
        long l = cal.getTimeInMillis();
        long a =o.getTime();
        long b = a/1000/60/60/24;//换算为天
        return b/365+"岁";
    }

}
