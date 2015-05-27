package com.company.runman.datacenter.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by EdisonZhao on 14-8-7.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorHostSystemInfoEntity extends BaseResultEntity implements Serializable {
    private List<String> iPAddress;//IP列表
    private String operationSystem;//操作系统
    private String operationVersion;//操作系统版本号
    private String platform;//操作系统平台
    private String servicePackage;//操作系统补丁包
    private String netVersion;//.NET Framework 框架版本
    private String isX64;//是否为64位操作系统
    private String manufacturer;//生产厂商
    private String model;//设备型号
    private String cPUSpeed;//CPU速度，单位MHZ
    private String cPUCount;//CPU内核数
    private String totalMemorySize;//物理内存，单位byte
    private String totalStorageSize;//物理存储，单位byte
    private String totalFreeStorageSpace;//剩余空间，单位byte
    private List<MonitorHostDiskInfoEntity> diskList;//磁盘列表

    public List<String> getiPAddress() {
        return iPAddress;
    }

    public void setiPAddress(List<String> iPAddress) {
        this.iPAddress = iPAddress;
    }

    public String getOperationSystem() {
        return operationSystem;
    }

    public void setOperationSystem(String operationSystem) {
        this.operationSystem = operationSystem;
    }

    public String getOperationVersion() {
        return operationVersion;
    }

    public void setOperationVersion(String operationVersion) {
        this.operationVersion = operationVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }

    public String getNetVersion() {
        return netVersion;
    }

    public void setNetVersion(String netVersion) {
        this.netVersion = netVersion;
    }

    public String getIsX64() {
        return isX64;
    }

    public void setIsX64(String isX64) {
        this.isX64 = isX64;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getcPUSpeed() {
        return cPUSpeed;
    }

    public void setcPUSpeed(String cPUSpeed) {
        this.cPUSpeed = cPUSpeed;
    }

    public String getcPUCount() {
        return cPUCount;
    }

    public void setcPUCount(String cPUCount) {
        this.cPUCount = cPUCount;
    }

    public String getTotalMemorySize() {
        return totalMemorySize;
    }

    public void setTotalMemorySize(String totalMemorySize) {
        this.totalMemorySize = totalMemorySize;
    }

    public String getTotalStorageSize() {
        return totalStorageSize;
    }

    public void setTotalStorageSize(String totalStorageSize) {
        this.totalStorageSize = totalStorageSize;
    }

    public String getTotalFreeStorageSpace() {
        return totalFreeStorageSpace;
    }

    public void setTotalFreeStorageSpace(String totalFreeStorageSpace) {
        this.totalFreeStorageSpace = totalFreeStorageSpace;
    }

    public List<MonitorHostDiskInfoEntity> getDiskList() {
        return diskList;
    }

    public void setDiskList(List<MonitorHostDiskInfoEntity> diskList) {
        this.diskList = diskList;
    }
}
