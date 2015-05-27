package com.company.runman.net.request;

import android.content.Context;
import com.company.runman.datacenter.model.MonitorHostEventEntity;
import com.company.runman.datacenter.provider.SharePreferenceProvider;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.utils.Constant;
import com.company.runman.utils.LogHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EdisonZhao on 14-8-8.
 * Email:zhaoliangyu@sobey.com
 * 获取最新事件
 */
public class MonitorNewEventRequest extends MonitorBaseRequest {

    private String station;
    private long time;//24小时之前,获取一天内的数据

    public MonitorNewEventRequest(Context context) {
        super(context);
    }

    @Override
    public void setData(Object obj) {
        super.setData(obj);
        if (obj instanceof String) {
            station = obj.toString();
        }
        time = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
    }

    @Override
    public String getUrl() {
        StringBuffer sb = new StringBuffer();
        params.put("time", time);
        params.put("station", station);
        String baseUrl = createBaseUrl(params);
        return sb.append(openHost)
                .append("alert/new?")
                .append(baseUrl).toString();
    }

    public static class MonitorNewEventResponse extends IResponse {

        private List<MonitorHostEventEntity> hostEventEntityList = new ArrayList<MonitorHostEventEntity>();

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            if (data instanceof String) {
                try {
                    JSONObject jsonData = new JSONObject(data.toString());
                    JSONArray jsonArray = jsonData.optJSONArray("Events");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        String eventID = jsonObject.optString("EventID");//事件唯一ID
                        String typeID = jsonObject.optString("TypeID");//事件类型ID
                        String level = jsonObject.optString("Level");//事件级别
                        String resourceKey = jsonObject.optString("ResourceKey");//关联资源标识
                        String stationCode = jsonObject.optString("StationCode");//电视台编码
                        String hostKey = jsonObject.optString("HostKey");//关联设备标识
                        String relativeEventID = jsonObject.optString("RelativeEventID");//关联事件ID
                        String time = jsonObject.optString("Time");//时间
                        String solutionTime = jsonObject.optString("SolutionTime");//解决时间
                        String description = jsonObject.optString("Description");//描述
                        String reason = jsonObject.optString("Reason");//事件说明,事件内容取此值
                        String solution = jsonObject.optString("Solution");//解决方案
                        String processingResult = jsonObject.optString("ProcessingResult");//处理结果
                        String relativeData = jsonObject.optString("RelativeData");//关联数据
                        String source = jsonObject.optString("Source");//来源
                        MonitorHostEventEntity eventEntity = new MonitorHostEventEntity();
                        eventEntity.setEventID(eventID);
                        eventEntity.setTypeID(typeID);
                        eventEntity.setLevel(level);
                        eventEntity.setResourceKey(resourceKey);
                        eventEntity.setStationCode(stationCode);
                        eventEntity.setHostKey(hostKey);
                        eventEntity.setRelativeEventID(relativeEventID);
                        eventEntity.setTime(time);
                        eventEntity.setSolutionTime(solutionTime);
                        eventEntity.setDescription(description);
                        eventEntity.setReason(reason);
                        eventEntity.setSolution(solution);
                        eventEntity.setProcessingResult(processingResult);
                        eventEntity.setRelativeData(relativeData);
                        eventEntity.setSource(source);
                        hostEventEntityList.add(eventEntity);
                    }
                } catch (JSONException e) {
                    LogHelper.e(TAG, "", e);
                }
            }
            return hostEventEntityList;
        }
    }
}
