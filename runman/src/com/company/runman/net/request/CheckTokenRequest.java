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
 * Created by Edison on 2014/6/10.
 */
public class CheckTokenRequest extends IRequest {

    private Context context;

    public CheckTokenRequest(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void setData(Object accessToken) {

    }

    @Override
    public String getUrl() {
        StringBuffer sb = new StringBuffer();
        String api = "rest/s/login/a/checktoken/invoke.json";
        String loginHost = SharePreferenceProvider.getInstance(context).readData(Constant.Host.LOGIN_HOST_KEY);
        return sb.append(loginHost).append(api).append("?authortoken=").append(accessToken).toString();
    }

    @Override
    public String getPost() {
        return null;
    }

    @Override
    public int getHttpMode() {
        return Constant.RequestCode.REQUEST_GET;
    }

    public static final class CheckTokenResponse extends IResponse {

        private AccountEntity accountEntity;

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            if (data != null) {
                try {
                    JSONObject jsonObject = new JSONObject(data.toString());
                    JSONObject userJson = jsonObject.optJSONObject("userEntity");
                    String nickname = userJson.optString("fullname");
                    accountEntity = new AccountEntity();
                    accountEntity.setFullname(nickname);
                    accountEntity.setSitecode(userJson.optString("sitecode"));
                    accountEntity.setAuthoritylist(userJson.optString("authoritylist"));
                    isSuccess = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return accountEntity;
        }
    }
}
