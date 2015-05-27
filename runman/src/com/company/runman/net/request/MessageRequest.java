package com.company.runman.net.request;

import android.content.Context;
import com.company.runman.datacenter.model.MessageEntity;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IRequest;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.utils.Constant;
import com.company.runman.utils.LogHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EdisonZhao on 14-8-5.
 * Email:zhaoliangyu@sobey.com
 */
public class MessageRequest extends IRequest {

    public MessageRequest(Context context) {
        super(context);
    }

    @Override
    public void setData(Object object) {
    }

    @Override
    public String getUrl() {
        StringBuffer sb = new StringBuffer();
        String api = "rest/msg.json";
        return sb.append(Constant.Host.BASE_HOST)
                .append(api).append("?access_token=")
                .append(accessToken)
                .append("&queryType=").toString();
    }

    @Override
    public String getPost() {
        return null;
    }

    @Override
    public int getHttpMode() {
        return Constant.RequestCode.REQUEST_GET;
    }

    public static final class MessageResponse extends IResponse {

        private List<MessageEntity> list = new ArrayList<MessageEntity>();

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            if (data != null) {
                try {
                    JSONObject jsonObject = new JSONObject(data.toString());
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject messageJson = jsonArray.getJSONObject(i);
                            String id = messageJson.optString("id");
                            String msg = messageJson.optString("msg");
                            String title = messageJson.optString("title");
                            String json = messageJson.optString("json");
                            String isread = messageJson.optString("isread");
                            int type = messageJson.optInt("type");
                            MessageEntity messageEntity = new MessageEntity();
                            messageEntity.setId(id);
                            messageEntity.setMsg(msg);
                            messageEntity.setTitle(title);
                            messageEntity.setJson(json);
                            messageEntity.setIsread(isread);
                            messageEntity.setType(type);
                            list.add(messageEntity);
                        }
                    }
                    isSuccess = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
    }
}
