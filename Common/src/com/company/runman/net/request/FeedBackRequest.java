package com.company.runman.net.request;

import android.content.ContentValues;
import android.content.Context;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IRequest;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.utils.Constant;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

/**
 * Created by EdisonZhao on 14-10-24.
 * Email:zhaoliangyu@sobey.com
 */
public class FeedBackRequest extends IRequest {

    ContentValues contentValues;

    public FeedBackRequest(Context context) {
        super(context);
    }

    @Override
    public void setData(Object data) {
        if (data instanceof ContentValues) {
            contentValues = (ContentValues) data;
        }
    }

    @Override
    public String getUrl() {
        StringBuffer sb = new StringBuffer();
        String api = "rest/saveOpinion.json";
        return sb.append(Constant.Host.BASE_HOST)
                .append(api)
                .append("?access_token=").append(accessToken).toString();
    }

    @Override
    public String getPost() {
        JSONObject postJson = new JSONObject();
        try {
            Set<String> set = contentValues.keySet();
            for (String key : set) {
                postJson.put(key, contentValues.getAsString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postJson.toString();
    }

    @Override
    public int getHttpMode() {
        return Constant.RequestCode.REQUEST_POST;
    }

    public static final class FeedBackResponse extends IResponse {

        ContentValues contentValues = new ContentValues();

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            if (data != null) {
                try {
                    JSONObject jsonObject = new JSONObject(data.toString());
                    JSONObject object = jsonObject.optJSONObject("ResponseMessage");
                    if (null != object) {
                        if (object.optString(Constant.ResponseData.STATUS).equals(Constant.ResponseData.STATUS_SUCCESS)) {
                            //反馈成功
                            contentValues.put(Constant.ResponseData.STATUS, true);
                        } else {
                            contentValues.put(Constant.ResponseData.STATUS, false);
                        }
                        JSONObject msgJson = object.optJSONObject("message");
                        if (msgJson != null) {
                            contentValues.put(Constant.ResponseData.MESSAGE, msgJson.optString("zh_CN"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return contentValues;
        }
    }
}
