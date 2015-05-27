package com.company.runman.net.request;

import android.content.Context;
import com.company.runman.datacenter.model.MonitorQueryEventRequestEntity;
import com.company.runman.net.HttpReturn;
import com.company.runman.utils.Constant;

/**
 * Created by EdisonZhao on 14-8-21.
 * Email:zhaoliangyu@sobey.com
 * 监控事件查询
 */
public class MonitorQueryEventRequest extends MonitorBaseRequest {

    private MonitorQueryEventRequestEntity requestEntity;

    public MonitorQueryEventRequest(Context context) {
        super(context);
    }

    @Override
    public void setData(Object obj) {
        super.setData(obj);
        if (obj instanceof MonitorQueryEventRequestEntity) {
            requestEntity = (MonitorQueryEventRequestEntity) obj;
        }
    }

    @Override
    public String getUrl() {
        StringBuffer sb = new StringBuffer();
        params.put("begin", requestEntity.getBegin());
        params.put("end", requestEntity.getEnd());
        params.put("station", requestEntity.getStation());
        params.put("hostkey", requestEntity.getHostkey());
        params.put("etype", requestEntity.getEtype());
        params.put("level", requestEntity.getLevel());
        params.put("pageindex", requestEntity.getPageindex());
        params.put("pagesize", requestEntity.getPagesize());
        String baseUrl = createBaseUrl(params);
        return sb.append(openHost)
                .append("alert?")
                .append(baseUrl).toString();
    }

    public static final class MonitorQueryEventResponse extends MonitorNewEventRequest.MonitorNewEventResponse {

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            return super.parseFrom(httpReturn);
        }
    }
}
