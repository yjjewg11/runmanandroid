package com.company.runman.net.request;

import android.content.Context;
import com.company.runman.datacenter.model.MonitorHostEntity;
import com.company.runman.net.HttpReturn;
import com.company.runman.utils.Constant;
import com.company.runman.utils.LogHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by EdisonZhao on 14-8-7.
 * Email:zhaoliangyu@sobey.com
 * 获取设备详细信息
 */
public class MonitorHostDetailRequest extends MonitorBaseRequest {

    private String hostkey;

    public MonitorHostDetailRequest(Context context) {
        super(context);
    }

    @Override
    public void setData(Object obj) {
        super.setData(obj);
        if (obj instanceof String) {
            hostkey = obj.toString();
        }
    }

    @Override
    public String getUrl() {
        StringBuffer sb = new StringBuffer();
        String baseUrl = createBaseUrl(params);
        String finalUrl = sb.append(openHost)
                .append("host/{ids}?")
                .append(baseUrl).toString();
        return finalUrl.replace("{ids}", hostkey);
    }

    public static final class MonitorHostDetailResponse extends MonitorBaseResponse {

        private MonitorHostEntity monitorHostEntity;

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            if (data instanceof String) {
                try {
                    JSONObject jsonObject = new JSONObject(data.toString());
                    boolean requestIsSuccess = jsonObject.optBoolean("IsSuccess");
                    if (requestIsSuccess) {
                        JSONObject hostJson = jsonObject.getJSONObject("HostInfo");
                        monitorHostEntity = parseMonitorHost(hostJson);
                    }
                    isSuccess = true;
                } catch (JSONException e) {
                    LogHelper.e(TAG, "", e);
                }
            }
            return monitorHostEntity;
        }
    }
}
