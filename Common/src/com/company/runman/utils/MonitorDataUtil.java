package com.company.runman.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by EdisonZhao on 14-8-8.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorDataUtil {

    //设备状态数据字典
    private static Map hostStatusMap = new HashMap<String, String>();

    static {

        hostStatusMap.put("Unknown", "未知");
        hostStatusMap.put("Normal", "正常");
        hostStatusMap.put("Paused", "暂停");
        hostStatusMap.put("NotStartup", "未启动");
        hostStatusMap.put("Abnormal", "异常");
    }

    public static String getHostStatus(String key) {
        Object result = hostStatusMap.get(key);
        if (result != null) {
            return result.toString();
        }
        return "null";
    }

    //事件级别数据字典
    private static Map eventLevelMap = new HashMap<String, String>();

    static {

        eventLevelMap.put("Unknown", "未知");
        eventLevelMap.put("Good", "修复通知");
        eventLevelMap.put("Info", "通知");
        eventLevelMap.put("Caution", "注意");
        eventLevelMap.put("Warnning", "警告");
        eventLevelMap.put("Error", "错误");
        eventLevelMap.put("Failure", "失败");
    }

    public static String getEventLevelStatus(String key) {
        Object result = eventLevelMap.get(key);
        if (result != null) {
            return result.toString();
        }
        return "null";
    }


    //处理结果数据字典
    private static Map processResultMap = new HashMap<String, String>();

    static {

        processResultMap.put("NotProcessing", "未处理");
        processResultMap.put("Ignore", "忽略");
        processResultMap.put("Processed", "已处理");
    }

    public static String getProcessResult(String key) {
        Object result = processResultMap.get(key);
        if (result != null) {
            return result.toString();
        }
        return "null";
    }
}
