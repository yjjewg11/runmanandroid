package com.company.runman.net.request;

import android.content.Context;
import com.company.runman.net.interfaces.IRequest;

/**
 * Created by Administrator on 2015/5/20.
 */
public class DefaultRequest extends IRequest {
    public DefaultRequest(Context context,String reuqestUrl,int httpMode) {
        super(context);
        this.reuqestUrl=reuqestUrl;
        this.httpMode=httpMode;
    }
}
