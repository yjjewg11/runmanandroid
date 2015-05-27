package com.company.runman.datacenter.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by EdisonZhao on 14-8-22.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorHostStatsEntity extends BaseResultEntity implements Serializable {

    private int count;//数量
    private List<MonitorHostEntity> hostEntityList;//数量
    private List<MonitorStationEntity> stationList;//电视台列表

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<MonitorHostEntity> getHostEntityList() {
        return hostEntityList;
    }

    public void setHostEntityList(List<MonitorHostEntity> hostEntityList) {
        this.hostEntityList = hostEntityList;
    }

    public List<MonitorStationEntity> getStationList() {
        return stationList;
    }

    public void setStationList(List<MonitorStationEntity> stationList) {
        this.stationList = stationList;
    }
}
