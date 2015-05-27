package com.company.runman.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.company.runman.net.interfaces.IRequest;
import com.company.runman.utils.Constant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by Edison on 2014/6/6.
 */
public class HttpHandler {


    /**
     * 一般的网络调用，如果需要登录，则重新登录，并重新调用接口
     */
    public static HttpReturn executeHttpGet(IRequest request) {
        HttpReturn result = new HttpReturn();
        String url = request.getUrl();
        if (TextUtils.isEmpty(url) || url.equals("null")) {
            result.code = Constant.ResponseCode.TIMEOUT;
            result.err = "empty url";
            return result;
        }
        result = NetworkHttpRequest.executeHttpGet(url);
        return result;
    }

    /**
     * 一般的网络调用（post方法），如果需要登录，则重新登录，并重新调用接口
     */
    public static HttpReturn executeHttpPost(IRequest request, Context context) {
        HttpReturn ret = new HttpReturn();
        String url = request.getUrl();
        if (TextUtils.isEmpty(url) || url.equals("null")) {
            ret.code = Constant.ResponseCode.TIMEOUT;
            ret.err = "empty url";
            return ret;
        }
        String post = request.getPost();
        ret = NetworkHttpRequest.executeHttpPost(context, url, post);
        return ret;
    }

    /**
     * 特殊的网络调用（put方法），如果需要登录，则重新登录，并重新调用接口
     */
    public static HttpReturn executeHttpPut(IRequest request, Context context) {
        HttpReturn ret = new HttpReturn();
        String url = request.getUrl();
        if (TextUtils.isEmpty(url) || url.equals("null")) {
            ret.code = Constant.ResponseCode.UNKNOWN_HOST;
            ret.err = "empty url";
            return ret;
        }
        String post = request.getPost();
        ret = NetworkHttpRequest.executeHttpPut(context, url, post);
        return ret;
    }
}
