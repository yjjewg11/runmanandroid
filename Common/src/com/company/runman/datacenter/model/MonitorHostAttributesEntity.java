package com.company.runman.datacenter.model;

import java.io.Serializable;

/**
 * Created by EdisonZhao on 14-8-7.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorHostAttributesEntity extends BaseResultEntity implements Serializable {

    private String code;//编码
    private String name;//名称
    private String value;//值

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
