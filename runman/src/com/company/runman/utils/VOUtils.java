package com.company.runman.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2015/5/28.
 * 业务数据转成用户展示数据
 */
public class VOUtils {
    static Map<String,Object> cacheMap=new ConcurrentHashMap<String,Object>();
    static{
        //TrainingPlan--订单状态：0：未发布，1：发布，2：待支付（已接单），3.支付完成，4：训练完成,5:关闭
        cacheMap.put("TrainPlan_0","未发布");
        cacheMap.put("TrainPlan_1","发布");
        cacheMap.put("TrainPlan_2","待支付");
        cacheMap.put("TrainPlan_3","支付完成");
        cacheMap.put("TrainPlan_4","训练完成");
        cacheMap.put("TrainPlan_5","关闭");

        //UserRelationTrainingCourse--订单状态：2：已预订，3：待支付，4：支付完成，5：课程完成,6:关闭。
        cacheMap.put("URTC_2","已预订");
        cacheMap.put("URTC_3","待支付");
        cacheMap.put("URTC_4","支付完成");
        cacheMap.put("URTC_5","课程完成");
        cacheMap.put("URTC_6","关闭");
    }

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

    /**
     *
     * @param o
     * @return
     */
    static public String userRelCourseStatusToString(Integer o){
        if(o==null)return "";
        return (String)cacheMap.get("URTC_"+o);
    }

    static public String trainingPlanStatusToString(Integer o){
        if(o==null)return "";
        return (String)cacheMap.get("TrainPlan_"+o);
    }

}
