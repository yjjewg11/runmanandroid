package com.company.runman.datacenter.model;

import java.io.Serializable;

/**
 * Created by EdisonZhao on 14-8-7.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorHostLoadInfoEntity extends BaseResultEntity implements Serializable {
    private String cPUUsedPercent;//CPU使用百分比， 0-100
    private String usedMemoryPercent;//已使用内存百分比， 0-100
    private String usedMemoryBytes;//已使用内存量，byte
    private String totalUsedStorageSpace;//已使用存储空间，byte
    private String usedStoragePercent;//已使用存储百分比
    private String totalFreeStorageSpace;//剩余存储百分比
    private String processCount;//当前进程数

    public String getcPUUsedPercent() {
        return cPUUsedPercent;
    }

    public void setcPUUsedPercent(String cPUUsedPercent) {
        this.cPUUsedPercent = cPUUsedPercent;
    }

    public String getUsedMemoryPercent() {
        return usedMemoryPercent;
    }

    public void setUsedMemoryPercent(String usedMemoryPercent) {
        this.usedMemoryPercent = usedMemoryPercent;
    }

    public String getUsedMemoryBytes() {
        return usedMemoryBytes;
    }

    public void setUsedMemoryBytes(String usedMemoryBytes) {
        this.usedMemoryBytes = usedMemoryBytes;
    }

    public String getTotalUsedStorageSpace() {
        return totalUsedStorageSpace;
    }

    public void setTotalUsedStorageSpace(String totalUsedStorageSpace) {
        this.totalUsedStorageSpace = totalUsedStorageSpace;
    }

    public String getUsedStoragePercent() {
        return usedStoragePercent;
    }

    public void setUsedStoragePercent(String usedStoragePercent) {
        this.usedStoragePercent = usedStoragePercent;
    }

    public String getTotalFreeStorageSpace() {
        return totalFreeStorageSpace;
    }

    public void setTotalFreeStorageSpace(String totalFreeStorageSpace) {
        this.totalFreeStorageSpace = totalFreeStorageSpace;
    }

    public String getProcessCount() {
        return processCount;
    }

    public void setProcessCount(String processCount) {
        this.processCount = processCount;
    }
}
