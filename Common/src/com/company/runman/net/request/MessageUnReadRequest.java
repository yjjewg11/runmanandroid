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
 * Created by EdisonZhao on 14-8-6.
 * Email:zhaoliangyu@sobey.com
 */
public class MessageUnReadRequest extends IRequest {

    public MessageUnReadRequest(Context context) {
        super(context);
    }

    @Override
    public void setData(Object object) {
    }

    @Override
    public String getUrl() {
        StringBuffer sb = new StringBuffer();
        String api = "rest/msg/queryNotReadCount.json";
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

        private int count = 0;

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            if (data != null) {
                try {
                    JSONObject jsonObject = new JSONObject(data.toString());
                    count = jsonObject.optInt("totalCount");
                    isSuccess = true;
                } catch (JSONException e) {
                    LogHelper.e(TAG, "", e);
                }
            }
            return count > 0 ? count : null;
        }
    }
}
