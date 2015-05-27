package com.company.runman.net.request;

import android.content.Context;
import com.company.runman.datacenter.model.AccountEntity;
import com.company.runman.datacenter.provider.SharePreferenceProvider;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IRequest;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.utils.Constant;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by EdisonZhao on 14-9-5.
 * Email:zhaoliangyu@sobey.com
 */
public class LoginOutRequest extends IRequest {

    private Context context;

    public LoginOutRequest(Context context) {
        super(context);
        this.context = context;
    }


    @Override
    public String getUrl() {
        StringBuffer sb = new StringBuffer();
        String api = "rest/userinfo/logout.json";
        String loginHost = SharePreferenceProvider.getInstance(context).readData(Constant.Host.LOGIN_HOST_KEY);
        return sb.append(this.getHost()).append(api).append("?"+Constant.SharePreferenceKey.KEY_JSESSIONID+"=").append(SharePreferenceProvider.getInstance(context).getJSESSIONID()).toString();
    }

    @Override
    public String getPost() {
        return null;
    }

    @Override
    public int getHttpMode() {
        return Constant.RequestCode.REQUEST_GET;
    }

    public static final class LogintOutResponse extends IResponse {


    }
}
