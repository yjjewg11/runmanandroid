package com.company.runman.datacenter.model;

import java.io.Serializable;

/**
 * Created by EdisonZhao on 14-11-3.
 * Email:zhaoliangyu@sobey.com
 */
public class KeywordEntity extends BaseResultEntity implements Serializable {

    private int _id;
    private String userCode;
    private String keyword;
    private String timestamp;
    private int count;

    public KeywordEntity() {
        _id = 0;
        userCode = "";
        keyword = "";
        timestamp = "0";
        count = 1;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
