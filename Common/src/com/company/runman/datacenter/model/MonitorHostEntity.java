package com.company.runman.datacenter.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by EdisonZhao on 14-8-7.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorHostEntity extends BaseResultEntity implements Serializable {

    private String hostKey;//设备唯一标识
    private String hostName;//设备名称
    private String interIPAddress;//内网IP
    private String stationCode;//电视台编码
    private String status;//设备状态
    private MonitorHostSystemInfoEntity systemInfo;//系统信息
    private MonitorHostLoadInfoEntity loadInfo;//负载信息
    private MonitorHostAddressInfoEntity position;//地理位置
    private String remoteIP;//外网IP地址
    private List<MonitorHostAttributesEntity> attributes;//附加属性

    public String getHostKey() {
        return hostKey;
    }

    public void setHostKey(String hostKey) {
        this.hostKey = hostKey;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getInterIPAddress() {
        return interIPAddress;
    }

    public void setInterIPAddress(String interIPAddress) {
        this.interIPAddress = interIPAddress;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MonitorHostSystemInfoEntity getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(MonitorHostSystemInfoEntity systemInfo) {
        this.systemInfo = systemInfo;
    }

    public MonitorHostLoadInfoEntity getLoadInfo() {
        return loadInfo;
    }

    public void setLoadInfo(MonitorHostLoadInfoEntity loadInfo) {
        this.loadInfo = loadInfo;
    }

    public MonitorHostAddressInfoEntity getPosition() {
        return position;
    }

    public void setPosition(MonitorHostAddressInfoEntity position) {
        this.position = position;
    }

    public String getRemoteIP() {
        return remoteIP;
    }

    public void setRemoteIP(String remoteIP) {
        this.remoteIP = remoteIP;
    }

    public List<MonitorHostAttributesEntity> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<MonitorHostAttributesEntity> attributes) {
        this.attributes = attributes;
    }
}
