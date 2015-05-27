package com.company.runman.net.request;

import android.content.Context;
import com.company.news.jsonform.UserRegJsonform;
import com.company.runman.datacenter.model.AccountEntity;
import com.company.runman.datacenter.model.RegisterResponseEntity;
import com.company.runman.datacenter.provider.SharePreferenceProvider;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IRequest;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.utils.Constant;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by EdisonZhao on 14-9-4.
 * Email:zhaoliangyu@sobey.com
 */
public class RegisterRequest extends IRequest {

    private Context context;

    public RegisterRequest(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public String getUrl() {
        return this.getHost() + "rest/userinfo/reg.json";
    }

    @Override
    public int getHttpMode() {
        return Constant.RequestCode.REQUEST_POST;
    }


}
