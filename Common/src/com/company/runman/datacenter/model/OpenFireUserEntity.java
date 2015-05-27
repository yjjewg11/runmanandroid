package com.company.runman.datacenter.model;

/**
 * Created by EdisonZhao on 14-8-5.
 * Email:zhaoliangyu@sobey.com
 */
public class OpenFireUserEntity {

    private String userName;
    private String passWord;
    private boolean isMessage;
    private String internalUrl;
    private int internalPort;
    private String externalUrl;
    private int externalPort;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public boolean isMessage() {
        return isMessage;
    }

    public void setMessage(boolean isMessage) {
        this.isMessage = isMessage;
    }

    public String getInternalUrl() {
        return internalUrl;
    }

    public void setInternalUrl(String internalUrl) {
        this.internalUrl = internalUrl;
    }

    public int getInternalPort() {
        return internalPort;
    }

    public void setInternalPort(int internalPort) {
        this.internalPort = internalPort;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public int getExternalPort() {
        return externalPort;
    }

    public void setExternalPort(int externalPort) {
        this.externalPort = externalPort;
    }
}
