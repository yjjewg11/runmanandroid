package com.company.runman.net.request;

import android.content.Context;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IRequest;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.utils.Constant;
import com.company.runman.utils.LogHelper;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by EdisonZhao on 14-8-5.
 * Email:zhaoliangyu@sobey.com
 */
public class MessageReadRequest extends IRequest {

    private String id;
    private String url = "rest/msg/read/{id}.json";

    public MessageReadRequest(Context context) {
        super(context);
    }

    @Override
    public void setData(Object object) {
        if (object instanceof String) {
            id = object.toString();
        }
    }

    @Override
    public String getUrl() {
        url = url.replace("{id}", id);
        StringBuffer sb = new StringBuffer();
        return sb.append(Constant.Host.BASE_HOST)
                .append(url)
                .append("?access_token=")
                .append(accessToken).toString();
    }

    @Override
    public String getPost() {
        return null;
    }

    @Override
    public int getHttpMode() {
        return Constant.RequestCode.REQUEST_PUT;
    }

    public static final class MessageReadResponse extends IResponse {

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            if (data != null) {
                try {
                    JSONObject jsonObject = new JSONObject(data.toString());
                    JSONObject json = jsonObject.optJSONObject("ResponseMessage");
                    String status = json.optString("status");
                    if ("success".equals(status)) {
                        isSuccess = true;
                    }
                } catch (JSONException e) {
                    LogHelper.e(TAG, "", e);
                }
            }
            return isSuccess;
        }
    }
}
