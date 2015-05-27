package com.company.runman.datacenter.model;

/**
 * Created by EdisonZhao on 14-8-11.
 * Email:zhaoliangyu@sobey.com
 */
public class UpgradeRequestEntity {

    private String versionName;
    private String appKey;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
