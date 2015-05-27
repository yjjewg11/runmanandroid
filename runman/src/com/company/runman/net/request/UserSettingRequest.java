package com.company.runman.net.request;

import android.content.ContentValues;
import android.content.Context;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IRequest;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.utils.Constant;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by EdisonZhao on 14/12/18.
 * Email:zhaoliangyu@sobey.com
 */
public class UserSettingRequest extends IRequest {

    public UserSettingRequest(Context context) {
        super(context);
    }

    @Override
    public void setData(Object T) {

    }

    @Override
    public String getUrl() {
        StringBuilder sb = new StringBuilder();
        String api = "rest/usersetting.json";
        String url = sb.append(Constant.Host.BASE_HOST)
                .append(api)
                .append("?access_token=").append(accessToken)
                        //如果 paraname不填值，或者不存在时，代表获取用户所有配置属性，如果有值则代表获取指定属性的值。
                        //.append("&paraname=").append("")
                .toString();
        return url;
    }

    @Override
    public String getPost() {
        return null;
    }

    @Override
    public int getHttpMode() {
        return Constant.RequestCode.REQUEST_GET;
    }

    public static final class UserSettingResponse extends IResponse {

        ContentValues contentValues = new ContentValues();

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            if (data != null) {
                try {
                    JSONObject jsonObject = new JSONObject(data.toString());
                    JSONObject object = jsonObject.optJSONObject("ResponseMessage");
                    String userData = jsonObject.optString("data");
                    contentValues.put(Constant.ResponseData.DATA, userData);
                    if (null != object) {
                        if (object.optString(Constant.ResponseData.STATUS).equals(Constant.ResponseData.STATUS_SUCCESS)) {
                            //保存成功
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
