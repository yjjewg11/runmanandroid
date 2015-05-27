package com.company.runman.net.request;

import android.content.Context;
import android.text.TextUtils;
import com.company.runman.datacenter.model.MonitorStatsAlertDBData;
import com.company.runman.datacenter.model.MonitorStatsEventDetailEntity;
import com.company.runman.datacenter.model.MonitorStatsEventEntity;
import com.company.runman.datacenter.model.MonitorStatsEventRequestEntity;
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
 * Created by EdisonZhao on 14-10-23.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorStatsListRequest extends MonitorBaseRequest {

    private List<MonitorStatsAlertDBData> requestEntityList;

    public MonitorStatsListRequest(Context context) {
        super(context);
    }

    @Override
    public void setData(Object obj) {
        super.setData(obj);
        if (obj instanceof ArrayList) {
            requestEntityList = (List<MonitorStatsAlertDBData>) obj;
        }
    }

    @Override
    public String getUrl() {
        String url = createBaseUrl();
        //最后返回数据,组合成最后的请求数据
        StringBuffer realUrl = new StringBuffer();
        realUrl.append(openHost).append("stats/alertList?");
        realUrl.append(url);
        return realUrl.toString();
    }

    @Override
    public String getPost() {
        JSONObject postData = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            if (null != requestEntityList && !requestEntityList.isEmpty()) {
                for (MonitorStatsAlertDBData requestEntity : requestEntityList) {
                    JSONObject valuesJson = new JSONObject();
                    valuesJson.put("station", requestEntity.getStationCode());
                    valuesJson.put("grouplevel", Constant.EventStatsType.Station);
                    if (1 == requestEntity.getIsReviewed()) {
                        valuesJson.put("begin", requestEntity.getTime());
                    }
                    jsonArray.put(valuesJson);
                }
            } else {
                JSONObject valuesJson = new JSONObject();
                valuesJson.put("grouplevel", Constant.EventStatsType.Station);
                jsonArray.put(valuesJson);
            }
            JSONObject postDataString = new JSONObject();
            postDataString.put("RequestItemList", jsonArray);
            postData.put(Constant.HTTP_POST_TYPE_JSON, true);
            postData.put(Constant.HTTP_POST_TYPE_JSON_DATA, postDataString.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData.toString();
    }

    @Override
    public int getHttpMode() {
        return Constant.RequestCode.REQUEST_POST;
    }

    public static final class MonitorStatsListResponse extends IResponse {

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
                                detailEntity.setTime(json.optLong("Time"));
                                detailEntity.setLastAlertTime(json.optLong("LastAlertTime"));
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
