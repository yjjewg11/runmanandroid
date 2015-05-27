package com.company.runman.net.request;

import android.content.Context;
import com.company.runman.datacenter.model.MonitorStationEntity;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.utils.Constant;
import com.company.runman.utils.LogHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by EdisonZhao on 14-8-6.
 * Email:zhaoliangyu@sobey.com
 * 获取电视台列表
 */
public class MonitorStationRequest extends MonitorBaseRequest {

    public MonitorStationRequest(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        StringBuffer sb = new StringBuffer();
        String baseUrl = createBaseUrl(params);
        return sb.append(openHost)
                .append("station?").append(baseUrl).toString();
    }

    public static final class MonitorStationResponse extends IResponse {

        private List<MonitorStationEntity> list = new ArrayList<MonitorStationEntity>();

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            if (data instanceof String) {
                try {
                    JSONObject jsonObject = new JSONObject(data.toString());
                    boolean requestIsSuccess = jsonObject.optBoolean("IsSuccess");
                    if (requestIsSuccess) {
                        JSONArray jsonArray = jsonObject.optJSONArray("StationList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject station = jsonArray.getJSONObject(i);
                            MonitorStationEntity tv = new MonitorStationEntity();
                            tv.setPosition(station.optString("Position"));
                            tv.setStationCode(station.optString("StationCode"));
                            tv.setStationName(station.optString("StationName"));
                            list.add(tv);
                        }
                    }
                } catch (JSONException e) {
                    LogHelper.e(TAG, "", e);
                }
            }
            return list;
        }
    }
}
