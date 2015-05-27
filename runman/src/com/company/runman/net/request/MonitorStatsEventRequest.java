package com.company.runman.net.request;

import android.content.Context;
import android.text.TextUtils;
import com.company.runman.datacenter.model.MonitorStatsEventDetailEntity;
import com.company.runman.datacenter.model.MonitorStatsEventEntity;
import com.company.runman.datacenter.model.MonitorStatsEventRequestEntity;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.utils.Constant;
import com.company.runman.utils.LogHelper;
import com.company.runman.utils.Tool;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EdisonZhao on 14-8-21.
 * Email:zhaoliangyu@sobey.com
 * 监控事件统计
 */
public class MonitorStatsEventRequest extends MonitorBaseRequest {

    private MonitorStatsEventRequestEntity requestEntity;

    public MonitorStatsEventRequest(Context context) {
        super(context);
    }

    @Override
    public void setData(Object obj) {
        super.setData(obj);
        if (obj instanceof MonitorStatsEventRequestEntity) {
            requestEntity = (MonitorStatsEventRequestEntity) obj;
        }
    }

    @Override
    public String getUrl() {
        params.put("begin", requestEntity.getBegin());
        params.put("end", requestEntity.getEnd());
        params.put("hostkey", requestEntity.getHostkey());
        params.put("grouplevel", requestEntity.getGrouplevel());
        String url = createBaseUrl(params);
        //最后返回数据,组合成最后的请求数据
        StringBuffer realUrl = new StringBuffer();
        realUrl.append(openHost).append("stats/alert?");
        realUrl.append(url);
        return realUrl.toString();
    }

    public static final class MonitorStatsEventResponse extends IResponse {

        private MonitorStatsEventEntity eventEntity = null;

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            if (data instanceof String) {
                try {
                    eventEntity = new MonitorStatsEventEntity();
                    JSONObject jsonObject = new JSONObject(data.toString());
                    eventEntity.setTime(jsonObject.optLong("Time"));
                    eventEntity.setGroupLevel(jsonObject.optString("GroupLevel"));
                    JSONArray jsonArray = jsonObject.optJSONArray("StatsList");
                    List<MonitorStatsEventDetailEntity> statsList = new ArrayList<MonitorStatsEventDetailEntity>();
                    if (null != jsonArray) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.optJSONObject(i);
                            if (null != json) {
                                MonitorStatsEventDetailEntity detailEntity = new MonitorStatsEventDetailEntity();
                                detailEntity.setGroupCode(json.optString("GroupCode"));
                                detailEntity.setKitCount(json.optInt("KitCount"));
                                detailEntity.setCount(json.optInt("Count"));
                                JSONArray countArray = json.optJSONArray("Detail");
                                for (int j = 0; j < countArray.length(); j++) {
                                    JSONObject countObj = countArray.optJSONObject(j);
                                    int count = countObj.optInt("Count");
                                    if (TextUtils.equals("Info", countObj.optString("Level"))) {
                                        detailEntity.setInfoCount(count);
                                    } else if (TextUtils.equals("Warnning", countObj.optString("Level"))) {
                                        detailEntity.setWarningCount(count);
                                    } else if (TextUtils.equals("Error", countObj.optString("Level"))) {
                                        detailEntity.setErrorCount(count);
                                    }
                                }
                                statsList.add(detailEntity);
                            }
                        }
                        eventEntity.setStatsList(statsList);
                    }
                } catch (JSONException e) {
                    LogHelper.d(TAG, "", e);
                }
            }
            return eventEntity;
        }
    }
}
