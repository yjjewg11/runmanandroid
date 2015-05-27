package com.company.runman.datacenter.model;

import java.io.Serializable;

/**
 * Created by EdisonZhao on 14-10-23.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorStatsAlertDBData extends BaseResultEntity implements Serializable {

    private String UserCode;
    private long Time;
    private long LastAlertTime;
    private int KitCount;
    private String StationCode;
    private int IsReviewed;

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public long getLastAlertTime() {
        return LastAlertTime;
    }

    public void setLastAlertTime(long lastAlertTime) {
        LastAlertTime = lastAlertTime;
    }

    public int getKitCount() {
        return KitCount;
    }

    public void setKitCount(int kitCount) {
        KitCount = kitCount;
    }

    public String getStationCode() {
        return StationCode;
    }

    public void setStationCode(String stationCode) {
        StationCode = stationCode;
    }

    public int getIsReviewed() {
        return IsReviewed;
    }

    public void setIsReviewed(int isReviewed) {
        IsReviewed = isReviewed;
    }
}
