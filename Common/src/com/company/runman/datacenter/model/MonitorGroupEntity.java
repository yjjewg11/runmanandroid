package com.company.runman.datacenter.model;

import java.io.Serializable;

/**
 * Created by EdisonZhao on 14-9-5.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorGroupEntity extends BaseResultEntity implements Serializable {

    private String stationCode;//电视台code
    private String code;//分组code
    private String name;//分组名称
    private String prior;//优先级

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

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

    public String getPrior() {
        return prior;
    }

    public void setPrior(String prior) {
        this.prior = prior;
    }
}
