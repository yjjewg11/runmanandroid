package com.company.runman.datacenter.model;

/**
 * Created by EdisonZhao on 14-8-6.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorStationEntity extends SortBaseEntity {

    private String stationCode; //电视台code
    private String stationName; //电视台名称
    private String position; //地理位置
    private boolean isChecked = false;//用于定制电视台的时候,区分是否被定制选中

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
