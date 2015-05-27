package com.company.runman.net;

/**
 * Created by Edison on 2014/6/6.
 */
public class HttpReturn {
    public int code = -1;
    public byte[] bytes;
    public String err;
    public long time;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public HttpReturn() {
        code = -1;
        bytes = new byte[0];
        time = 0;
        err = "";
    }

    public HttpReturn(int c) {
        code = c;
        bytes = new byte[0];
        time = 0;
        err = "";
    }

    public String body() {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        return new String(bytes);
    }

    @Override
    public String toString() {
        return "code:" + code + ", body:" + body();
    }

}
