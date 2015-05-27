package com.company.runman.datacenter.model;

import java.io.Serializable;

/**
 * Created by EdisonZhao on 14-8-8.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorHostEventEntity extends BaseResultEntity implements Serializable {
    private String eventID;//事件唯一ID
    private String typeID;//事件类型ID
    private String level;//事件级别
    private String resourceKey;//关联资源标识
    private String stationCode;//电视台编码
    private String hostKey;//关联设备标识
    private String relativeEventID;//关联事件ID
    private String time;//时间
    private String solutionTime;//解决时间
    private String description;//描述
    private String reason;//事件说明,事件内容取此值
    private String solution;//解决方案
    private String processingResult;//处理结果
    private String relativeData;//关联数据
    private String source;//来源

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getHostKey() {
        return hostKey;
    }

    public void setHostKey(String hostKey) {
        this.hostKey = hostKey;
    }

    public String getRelativeEventID() {
        return relativeEventID;
    }

    public void setRelativeEventID(String relativeEventID) {
        this.relativeEventID = relativeEventID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSolutionTime() {
        return solutionTime;
    }

    public void setSolutionTime(String solutionTime) {
        this.solutionTime = solutionTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getProcessingResult() {
        return processingResult;
    }

    public void setProcessingResult(String processingResult) {
        this.processingResult = processingResult;
    }

    public String getRelativeData() {
        return relativeData;
    }

    public void setRelativeData(String relativeData) {
        this.relativeData = relativeData;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
