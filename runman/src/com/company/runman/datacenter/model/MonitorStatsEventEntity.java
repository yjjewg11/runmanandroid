package com.company.runman.datacenter.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by EdisonZhao on 14-8-21.
 * Email:zhaoliangyu@sobey.com
 * 监控事件统计
 */
public class MonitorStatsEventEntity extends BaseResultEntity implements Serializable {
    private long time;
    private String groupLevel;
    private List<MonitorStatsEventDetailEntity> statsList;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getGroupLevel() {
        return groupLevel;
    }

    public void setGroupLevel(String groupLevel) {
        this.groupLevel = groupLevel;
    }

    public List<MonitorStatsEventDetailEntity> getStatsList() {
        return statsList;
    }

    public void setStatsList(List<MonitorStatsEventDetailEntity> statsList) {
        this.statsList = statsList;
    }
}
