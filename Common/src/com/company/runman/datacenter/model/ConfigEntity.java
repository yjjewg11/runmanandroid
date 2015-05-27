package com.company.runman.datacenter.model;

import java.io.Serializable;

/**
 * Created by EdisonZhao on 14-8-5.
 * Email:zhaoliangyu@sobey.com
 */
public class ConfigEntity extends BaseResultEntity {

    private String md5;
    private boolean isChanged;
    private String configJson;
    private String kLoginUrl; //统一认证访问接口
    private String kOpenUrl; //监控的url
    private String kOpenAppID; //监控的Appid
    private String kOpenAppKey; //监控的Appkey
    private String askUrl;//问答地址
    private String libUrl;//知识库地址
    private String cookieName;//
    private String cookieDomain;
    private String cookiePath;
    private String kDevUrl;//索贝社区
    private String kDocUrl;//错误码搜索地址
    private int kPullTime;//监控轮循时间间隔

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getCookieDomain() {
        return cookieDomain;
    }

    public void setCookieDomain(String cookieDomain) {
        this.cookieDomain = cookieDomain;
    }

    public String getCookiePath() {
        return cookiePath;
    }

    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    public String getkDevUrl() {
        return kDevUrl;
    }

    public void setkDevUrl(String kDevUrl) {
        this.kDevUrl = kDevUrl;
    }

    public int getkPullTime() {
        return kPullTime;
    }

    public void setkPullTime(int kPullTime) {
        this.kPullTime = kPullTime;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getkLoginUrl() {
        return kLoginUrl;
    }

    public void setkLoginUrl(String kLoginUrl) {
        this.kLoginUrl = kLoginUrl;
    }

    public String getkOpenUrl() {
        return kOpenUrl;
    }

    public void setkOpenUrl(String kOpenUrl) {
        this.kOpenUrl = kOpenUrl;
    }

    public String getkOpenAppID() {
        return kOpenAppID;
    }

    public void setkOpenAppID(String kOpenAppID) {
        this.kOpenAppID = kOpenAppID;
    }

    public String getkOpenAppKey() {
        return kOpenAppKey;
    }

    public void setkOpenAppKey(String kOpenAppKey) {
        this.kOpenAppKey = kOpenAppKey;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }

    public String getAskUrl() {
        return askUrl;
    }

    public void setAskUrl(String askUrl) {
        this.askUrl = askUrl;
    }

    public String getLibUrl() {
        return libUrl;
    }

    public void setLibUrl(String libUrl) {
        this.libUrl = libUrl;
    }

    public String getkDocUrl() {
        return kDocUrl;
    }

    public void setkDocUrl(String kDocUrl) {
        this.kDocUrl = kDocUrl;
    }
}
