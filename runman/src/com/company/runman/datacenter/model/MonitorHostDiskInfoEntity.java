package com.company.runman.datacenter.model;

import java.io.Serializable;

/**
 * Created by EdisonZhao on 14-8-7.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorHostDiskInfoEntity extends BaseResultEntity implements Serializable {

    private String DiskName;//磁盘名称
    private String TotalSize;//容量，单位byte
    private String FreeSpace;//剩余空间，单位byte
    private String UsedSpace;//已用空间，单位byte

    public String getDiskName() {
        return DiskName;
    }

    public void setDiskName(String diskName) {
        DiskName = diskName;
    }

    public String getTotalSize() {
        return TotalSize;
    }

    public void setTotalSize(String totalSize) {
        TotalSize = totalSize;
    }

    public String getFreeSpace() {
        return FreeSpace;
    }

    public void setFreeSpace(String freeSpace) {
        FreeSpace = freeSpace;
    }

    public String getUsedSpace() {
        return UsedSpace;
    }

    public void setUsedSpace(String usedSpace) {
        UsedSpace = usedSpace;
    }
}
