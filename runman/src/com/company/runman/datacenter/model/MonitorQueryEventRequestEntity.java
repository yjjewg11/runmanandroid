package com.company.runman.datacenter.model;

import java.io.Serializable;

/**
 * Created by EdisonZhao on 14-8-21.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorQueryEventRequestEntity extends BaseResultEntity implements Serializable {

    private long begin;//限定事件发生的开始区间
    private long end;//限定事件发生的结束区间
    private String station;//限制电视台编码，多个电视台编码之间以|号分隔
    private String hostkey;//限定事件对应设备
    private String etype;//限定事件的类型，多个事件类型之间以|号分隔
    private String level;//限定事件的级别，多个级别之间以|号分隔
    private int pageindex;//页面索引，从1开始
    private int pagesize;//每页显示数，默认30

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

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getHostkey() {
        return hostkey;
    }

    public void setHostkey(String hostkey) {
        this.hostkey = hostkey;
    }

    public String getEtype() {
        return etype;
    }

    public void setEtype(String etype) {
        this.etype = etype;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getPageindex() {
        return pageindex;
    }

    public void setPageindex(int pageindex) {
        this.pageindex = pageindex;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }
}
