package com.company.runman.net.request;

import android.content.Context;
import com.company.runman.datacenter.model.MonitorEventSummaryEntity;
import com.company.runman.datacenter.model.MonitorStatsEventRequestEntity;
import com.company.runman.datacenter.model.MonitorSummaryEntity;
import com.company.runman.datacenter.provider.SharePreferenceProvider;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.utils.Constant;
import com.company.runman.utils.LogHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EdisonZhao on 14-8-21.
 * Email:zhaoliangyu@sobey.com
 * 获得监控事件摘要
 */
public class MonitorEventSummaryRequest extends MonitorBaseRequest {

    private MonitorStatsEventRequestEntity requestEntity;

    public MonitorEventSummaryRequest(Context context) {
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
        StringBuffer sb = new StringBuffer();
        params.put("begin", requestEntity.getBegin());
        params.put("end", requestEntity.getEnd());
        params.put("station", requestEntity.getStation());
        params.put("hostkey", requestEntity.getHostkey());
        params.put("level", requestEntity.getLevel());
        String baseUrl = createBaseUrl(params);

        return sb.append(openHost)
                .append("alert/summary?")
                .append(baseUrl).toString();
    }

    public static final class MonitorEventSummaryResponse extends IResponse {

        private MonitorEventSummaryEntity summaryEntity = new MonitorEventSummaryEntity();

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            if (data instanceof String) {
                try {
                    JSONObject jsonObject = new JSONObject(data.toString());
                    summaryEntity.setTime(jsonObject.optLong("Time"));
                    JSONArray jsonArray = jsonObject.optJSONArray("SummaryList");
                    List<MonitorSummaryEntity> summaryList = new ArrayList<MonitorSummaryEntity>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        MonitorSummaryEntity monitorSummaryEntity = new MonitorSummaryEntity();
                        JSONObject summary = jsonArray.optJSONObject(i);
                        monitorSummaryEntity.setLastTime(summary.optString("LastTime"));
                        monitorSummaryEntity.setHostKey(summary.optString("HostKey"));
                        monitorSummaryEntity.setTypeID(summary.optString("TypeID"));
                        monitorSummaryEntity.setCount(summary.optInt("Count"));
                        monitorSummaryEntity.setUnProcessedCount(summary.optInt("UnProcessedCount"));
                        monitorSummaryEntity.setDesc(summary.optString("Desc"));
                        summaryList.add(monitorSummaryEntity);
                    }
                    summaryEntity.setSummaryList(summaryList);
                    JSONArray eventArray = jsonObject.optJSONArray("EventTypes");
                    Map<String, String> eventTypeMap = new HashMap<String, String>();
                    for (int j = 0; j < eventArray.length(); j++) {
                        String typeId = eventArray.optJSONObject(j).optString("TypeID");
                        String name = eventArray.optJSONObject(j).optString("Name");
                        eventTypeMap.put(typeId, name);
                    }
                    summaryEntity.setEventTypesMap(eventTypeMap);
                } catch (Exception e) {
                    LogHelper.e(TAG, "", e);
                }
            }
            return summaryEntity;
        }
    }
}
