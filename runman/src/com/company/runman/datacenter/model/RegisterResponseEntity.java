package com.company.runman.datacenter.model;

import java.io.Serializable;

/**
 * Created by LMQ on 14-9-4.
 * Email:
 */
public class RegisterResponseEntity extends BaseResultEntity implements Serializable {

    private boolean isSuccess;
    private String msg;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
