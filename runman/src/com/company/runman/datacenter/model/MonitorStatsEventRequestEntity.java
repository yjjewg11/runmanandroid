package com.company.runman.datacenter.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by EdisonZhao on 14-8-21.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorStatsEventRequestEntity extends BaseResultEntity implements Serializable {

    private long begin;//限定事件发生的开始区间
    private long end;//限定事件发生的结束区间
    private Map<String, Long> stationMap;//key电视台编码, value时间戳
    private String station;//key电视台编码, value时间戳
    private String hostkey;//限定事件对应设备
    private String grouplevel;//指定统计汇总级别，空或不传表示不分组汇总，若为station表示按电视台汇
    private String level;//限定监控事件的级别，多个级别以|号分隔

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public Map<String, Long> getStationMap() {
        return stationMap;
    }

    public void setStationMap(Map<String, Long> stationMap) {
        this.stationMap = stationMap;
    }

    public String getHostkey() {
        return hostkey;
    }

    public void setHostkey(String hostkey) {
        this.hostkey = hostkey;
    }

    public String getGrouplevel() {
        return grouplevel;
    }

    public void setGrouplevel(String grouplevel) {
        this.grouplevel = grouplevel;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
