package com.company.runman.net.request;

import android.content.Context;
import com.company.runman.datacenter.model.MonitorGroupEntity;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.utils.Constant;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EdisonZhao on 14-9-5.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorGroupRequest extends MonitorBaseRequest {

    private String stationKey = "";

    public MonitorGroupRequest(Context context) {
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
        String baseUrl = createBaseUrl(params);
        return sb.append(openHost)
                .append("station/group?")
                .append(baseUrl).toString();
    }

    public static final class MonitorGroupResponse extends IResponse {

        private List<MonitorGroupEntity> groupEntityList;

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            if (data instanceof String) {
                try {
                    groupEntityList = new ArrayList<MonitorGroupEntity>();
                    JSONObject jsonObject = new JSONObject(data.toString());
                    JSONArray groupJSON = jsonObject.optJSONArray("GroupList");
                    for (int i = 0; i < groupJSON.length(); i++) {
                        MonitorGroupEntity groupEntity = new MonitorGroupEntity();
                        JSONObject group = groupJSON.optJSONObject(i);
                        groupEntity.setStationCode(group.optString("StationCode"));
                        groupEntity.setName(group.optString("Name"));
                        groupEntity.setCode(group.optString("Code"));
                        groupEntity.setPrior(group.optString("Prior"));
                        groupEntityList.add(groupEntity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return groupEntityList;
        }
    }
}
