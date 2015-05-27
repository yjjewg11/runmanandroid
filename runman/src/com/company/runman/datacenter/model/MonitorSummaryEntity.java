package com.company.runman.datacenter.model;

import java.io.Serializable;

/**
 * Created by EdisonZhao on 14-8-22.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorSummaryEntity extends BaseResultEntity implements Serializable {

    private String lastTime;//当前结果结束时间
    private String hostKey;//设备编码
    private String typeID;//事件类别代码
    private int count;//数量
    private int unProcessedCount;//未处理数量
    private String desc;//最后事件描述

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getHostKey() {
        return hostKey;
    }

    public void setHostKey(String hostKey) {
        this.hostKey = hostKey;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getUnProcessedCount() {
        return unProcessedCount;
    }

    public void setUnProcessedCount(int unProcessedCount) {
        this.unProcessedCount = unProcessedCount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
