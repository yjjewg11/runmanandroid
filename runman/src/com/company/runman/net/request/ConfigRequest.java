package com.company.runman.net.request;

import android.content.Context;
import com.company.runman.datacenter.model.ConfigEntity;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IRequest;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.utils.Constant;
import com.company.runman.utils.LogHelper;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by EdisonZhao on 14-8-5.
 * Email:zhaoliangyu@sobey.com
 */
public class ConfigRequest extends IRequest {

    private String md5 = "";

    public ConfigRequest(Context context) {
        super(context);
    }

    @Override
    public void setData(Object object) {
        if (object instanceof String) {
            md5 = object.toString();
        }
    }

    @Override
    public String getUrl() {
        StringBuffer sb = new StringBuffer();
        String api = "rest/config.json";
        return sb.append(Constant.Host.BASE_HOST).append(api).append("?md5=").append(md5).toString();
    }

    @Override
    public String getPost() {
        return null;
    }

    @Override
    public int getHttpMode() {
        return Constant.RequestCode.REQUEST_GET;
    }

    public static final class ConfigResponse extends IResponse {

        private ConfigEntity configEntity;

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            if (data != null) {
                try {
                    JSONObject jsonObject = new JSONObject(data.toString());
                    configEntity = new ConfigEntity();
                    configEntity.setMd5(jsonObject.optString("md5"));
                    JSONObject configJson = jsonObject.optJSONObject("config");
                    if (configJson != null) {
                        configEntity.setkLoginUrl(configJson.optString("KLoginUrl"));
                        configEntity.setkOpenUrl(configJson.optString("KOpenUrl"));
                        configEntity.setkOpenAppID(configJson.optString("kOpenAppID"));
                        configEntity.setkOpenAppKey(configJson.optString("kOpenAppKey"));
                        configEntity.setAskUrl(configJson.optString("ask"));
                        configEntity.setLibUrl(configJson.optString("kb"));
                        configEntity.setkPullTime(configJson.optInt("kPullTime"));
                        configEntity.setCookieDomain(configJson.optString("cookieDomain"));
                        configEntity.setCookieName(configJson.optString("cookieName"));
                        configEntity.setCookiePath(configJson.optString("cookiePath"));
                        configEntity.setkDevUrl(configJson.optString("kDevUrl"));
                        configEntity.setkDocUrl(configJson.optString("kDocUrl"));
                        configEntity.setChanged(true);
                        configEntity.setConfigJson(configJson.toString());
                    } else {
                        configEntity.setChanged(false);
                    }
                    isSuccess = true;
                } catch (JSONException e) {
                    LogHelper.e(TAG, "", e);
                }
            }
            return configEntity;
        }
    }
}
