package com.company.runman.datacenter.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by EdisonZhao on 14-8-21.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorStatsEventDetailEntity extends BaseResultEntity implements Parcelable {

    private long time; //上一次获取报警信息的请求时间
    private long lastAlertTime; //最后一次的报警时间(用于排序)
    private String groupCode;//分组项,可能是设备,可能是电视台
    private int kitCount; //设备数量
    private int count; //告警数量
    private int infoCount; //普通告警数量
    private int warningCount;//严重告警数量
    private int errorCount;//重大报警数量

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getLastAlertTime() {
        return lastAlertTime;
    }

    public void setLastAlertTime(long lastAlertTime) {
        this.lastAlertTime = lastAlertTime;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public int getKitCount() {
        return kitCount;
    }

    public void setKitCount(int kitCount) {
        this.kitCount = kitCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getInfoCount() {
        return infoCount;
    }

    public void setInfoCount(int infoCount) {
        this.infoCount = infoCount;
    }

    public int getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(int warningCount) {
        this.warningCount = warningCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(groupCode);
        parcel.writeInt(kitCount);
        parcel.writeInt(count);
        parcel.writeInt(infoCount);
        parcel.writeInt(warningCount);
        parcel.writeInt(errorCount);
    }

    public static final Parcelable.Creator<MonitorStatsEventDetailEntity> CREATOR = new Parcelable.Creator<MonitorStatsEventDetailEntity>() {
        //重写Creator
        @Override
        public MonitorStatsEventDetailEntity createFromParcel(Parcel source) {
            MonitorStatsEventDetailEntity detail = new MonitorStatsEventDetailEntity();
            detail.groupCode = source.readString();
            detail.kitCount = source.readInt();
            detail.count = source.readInt();
            detail.infoCount = source.readInt();
            detail.warningCount = source.readInt();
            detail.errorCount = source.readInt();
            return detail;
        }

        @Override
        public MonitorStatsEventDetailEntity[] newArray(int size) {
            return null;
        }
    };
}
