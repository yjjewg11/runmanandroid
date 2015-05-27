package com.company.runman.net;

import android.content.Context;
import com.company.runman.net.interfaces.IRequest;
import com.company.runman.utils.Constant;
import com.company.runman.utils.LogHelper;
import com.company.runman.utils.Tool;
import com.company.runman.utils.TraceUtil;

import java.io.File;

/**
 * Created by Edison on 2014/6/6.
 */
public class HttpControl {

    public static HttpReturn execute(File file, String RequestURL) {
        TraceUtil.traceLog(RequestURL);
        HttpReturn ret = new HttpReturn();
        try {
            ret= NetworkHttpRequest.uploadFile(file,RequestURL);
        } catch (Exception e) {
            TraceUtil.traceThrowableLog(e);
            e.printStackTrace();
            ret.code = Constant.ResponseCode.TIMEOUT;
            ret.err = e.getMessage();
        }

        return ret;
    }
    public static HttpReturn execute(IRequest request, Context context) {
        HttpReturn ret = new HttpReturn();
        TraceUtil.traceLog(request.getUrl());
        try {
            int sMode = request.getHttpMode();
            if (sMode == Constant.RequestCode.REQUEST_GET) {
                ret = HttpHandler.executeHttpGet(request);
            } else if (sMode == Constant.RequestCode.REQUEST_POST) {
                ret = HttpHandler.executeHttpPost(request, context);
            } else if (sMode == Constant.RequestCode.REQUEST_PUT) {
                ret = HttpHandler.executeHttpPut(request, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret.code = Constant.ResponseCode.TIMEOUT;
            ret.err = e.getMessage();

            TraceUtil.traceThrowableLog(e);

            if (Tool.is2GNetWork()) {
                try {
                    int sMode = request.getHttpMode();
                    if (sMode == Constant.RequestCode.REQUEST_GET) {
                        ret = HttpHandler.executeHttpGet(request);
                    } else if (sMode == Constant.RequestCode.REQUEST_POST) {
                        ret = HttpHandler.executeHttpPost(request, context);
                    } else if (sMode == Constant.RequestCode.REQUEST_PUT) {
                        ret = HttpHandler.executeHttpPut(request, context);
                    }
                } catch (Exception e2) {
                    TraceUtil.traceThrowableLog(e);
                    ret.code = Constant.ResponseCode.TIMEOUT;
                    ret.err = e.getMessage();
                }
            }
        }
        return ret;
    }
}
