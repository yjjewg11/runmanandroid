package com.company.runman.net.request;

import android.content.Context;
import android.text.TextUtils;
import com.company.runman.datacenter.model.BaseResultEntity;
import com.company.runman.datacenter.model.MonitorHostEntity;
import com.company.runman.datacenter.model.MonitorHostStatsEntity;
import com.company.runman.datacenter.model.MonitorStationEntity;
import com.company.runman.net.HttpReturn;
import com.company.runman.utils.Constant;
import com.company.runman.utils.LogHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EdisonZhao on 14-8-7.
 * Email:zhaoliangyu@sobey.com
 * 获取设备列表
 */
public class MonitorHostRequest extends MonitorBaseRequest {

    private String stationKey = "";

    public MonitorHostRequest(Context context) {
        super(context);
    }

    @Override
    public void setData(Object obj) {
        super.setData(obj);
        if (obj instanceof String) {
            stationKey = obj.toString();
        }
    }

    @Override
    public String getUrl() {
        StringBuffer sb = new StringBuffer();
        params.put("station", stationKey);
        params.put("grouplevel", "station");
        String baseUrl = createBaseUrl(params);
        sb.append(openHost).append("host?").append(baseUrl).toString();
        return sb.toString();
    }

    public static final class MonitorHostResponse extends MonitorBaseResponse {

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            if (data instanceof String) {
                try {
                    JSONObject jsonObject = new JSONObject(data.toString());
                    boolean requestIsSuccess = jsonObject.optBoolean("IsSuccess");
                    if (requestIsSuccess) {
                        MonitorHostStatsEntity hostStatsEntity = new MonitorHostStatsEntity();
                        hostStatsEntity.setCount(jsonObject.optInt("Count"));
                        //获取所有设备列表
                        JSONArray hostArray = jsonObject.optJSONArray("HostList");
                        List<MonitorHostEntity> hostEntityList = new ArrayList<MonitorHostEntity>();
                        for (int i = 0; i < hostArray.length(); i++) {
                            JSONObject hostJson = hostArray.optJSONObject(i);
                            MonitorHostEntity hostEntity = parseMonitorHost(hostJson);
                            hostEntityList.add(hostEntity);
                        }
                        hostStatsEntity.setHostEntityList(hostEntityList);
                        //获取所有电视台列表
                        JSONArray stationArray = jsonObject.optJSONArray("StationList");
                        List<MonitorStationEntity> stationEntityList = new ArrayList<MonitorStationEntity>();
                        for (int j = 0; j < stationArray.length(); j++) {
                            JSONObject station = stationArray.getJSONObject(j);
                            MonitorStationEntity tv = new MonitorStationEntity();
                            tv.setPosition(station.optString("Position"));
                            tv.setStationCode(station.optString("StationCode"));
                            tv.setStationName(station.optString("StationName"));
                            stationEntityList.add(tv);
                        }
                        hostStatsEntity.setStationList(stationEntityList);
                        isSuccess = true;
                        return hostStatsEntity;
                    } else {
                        BaseResultEntity baseResultEntity = new BaseResultEntity();
                        String result = jsonObject.optString("Message");
                        int code = jsonObject.optInt("ErrorCode");
                        baseResultEntity.setResultMsg(result);
                        baseResultEntity.setResultCode(code);
                        isSuccess = false;
                        return baseResultEntity;
                    }
                } catch (JSONException e) {
                    LogHelper.e(TAG, "", e);
                }
            }
            return data;
        }
    }
}
