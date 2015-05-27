package com.company.runman.net.request;

import android.content.Context;
import com.company.runman.datacenter.model.MonitorStationHostEntity;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.utils.Constant;
import com.company.runman.utils.LogHelper;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by EdisonZhao on 14-8-21.
 * Email:zhaoliangyu@sobey.com
 * 电视台设备统计接口
 */
public class MonitorHostStatsRequest extends MonitorBaseRequest {

    private String stationCode;

    public MonitorHostStatsRequest(Context context) {
        super(context);
    }

    @Override
    public void setData(Object obj) {
        super.setData(obj);
        if (obj instanceof String) {
            stationCode = obj.toString();
        }
    }

    @Override
    public String getUrl() {
        StringBuffer sb = new StringBuffer();
        params.put("station", stationCode);
        String baseUrl = createBaseUrl(params);
        return sb.append(openHost)
                .append("stats/station?")
                .append(baseUrl).toString();
    }

    public static final class MonitorStationStatsResponse extends IResponse {

        public MonitorStationHostEntity stationHostEntity = new MonitorStationHostEntity();

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            if (data instanceof String) {
                try {
                    JSONObject jsonObject = new JSONObject(data.toString());
                    stationHostEntity.setTotal(jsonObject.optInt("Total"));
                    stationHostEntity.setOnline(jsonObject.optInt("Online"));
                    stationHostEntity.setOffline(jsonObject.optInt("Offline"));
                } catch (JSONException e) {
                    LogHelper.d(TAG, "", e);
                }
            }
            return stationHostEntity;
        }
    }
}
