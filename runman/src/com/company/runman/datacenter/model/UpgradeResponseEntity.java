package com.company.runman.datacenter.model;

import java.io.Serializable;

/**
 * Created by EdisonZhao on 14-8-11.
 * Email:zhaoliangyu@sobey.com
 */
public class UpgradeResponseEntity implements Serializable {

    private String description;//版本信息描述
    private String downLink;//最新版本的下载地址
    private String releaseTime;//客户端版本的发布时间
    private String size;//客户端安装包大小
    private String version;//客户端的最新版本号
    private String appid;//客户端版本的appid

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownLink() {
        return downLink;
    }

    public void setDownLink(String downLink) {
        this.downLink = downLink;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
