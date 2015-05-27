package com.company.runman.datacenter.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by EdisonZhao on 14-8-22.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorEventSummaryEntity extends BaseResultEntity implements Serializable {
    private long time;
    private List<MonitorSummaryEntity> summaryList;
    private Map<String, String> eventTypesMap;//事件类型id->事件类型名称

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<MonitorSummaryEntity> getSummaryList() {
        return summaryList;
    }

    public void setSummaryList(List<MonitorSummaryEntity> summaryList) {
        this.summaryList = summaryList;
    }

    public Map<String, String> getEventTypesMap() {
        return eventTypesMap;
    }

    public void setEventTypesMap(Map<String, String> eventTypesMap) {
        this.eventTypesMap = eventTypesMap;
    }
}
