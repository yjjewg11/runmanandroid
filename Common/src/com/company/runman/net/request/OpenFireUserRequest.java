package com.company.runman.net.request;

import android.content.Context;
import com.company.runman.datacenter.model.OpenFireUserEntity;
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
public class OpenFireUserRequest extends IRequest {

    public OpenFireUserRequest(Context context) {
        super(context);
    }

    @Override
    public void setData(Object object) {
        if (object instanceof String) {
            accessToken = object.toString();
        }
    }

    @Override
    public String getUrl() {
        StringBuffer sb = new StringBuffer();
        String api = "rest/msg/openfireUser.json";
        return sb.append(Constant.Host.BASE_HOST).append(api).append("?access_token=").append(accessToken).toString();
    }

    @Override
    public String getPost() {
        return null;
    }

    @Override
    public int getHttpMode() {
        return Constant.RequestCode.REQUEST_GET;
    }

    public static final class OpenFireUserResponse extends IResponse {

        private OpenFireUserEntity openFireUserEntity = new OpenFireUserEntity();
        private final String DISABLE = "disable";

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            if (data instanceof String) {
                try {
                    JSONObject jsonObject = new JSONObject(data.toString());
                    openFireUserEntity = new OpenFireUserEntity();
                    String username = jsonObject.optString("user");
                    String password = jsonObject.optString("pwd");
                    String internalUrl = jsonObject.optString("Openfire_HOST");
                    if (DISABLE.equals(internalUrl)) {
                        return openFireUserEntity;
                    } else {
                        openFireUserEntity.setMessage(true);
                    }
                    int internalPort = jsonObject.optInt("Openfire_HOST_PORT");
                    String externalUrl = jsonObject.optString("Openfire_HOST_Extranet");
                    int externalPort = jsonObject.optInt("Openfire_HOST_PORT_Extranet");
                    openFireUserEntity.setUserName(username);
                    openFireUserEntity.setPassWord(password);
                    openFireUserEntity.setInternalUrl(internalUrl);
                    openFireUserEntity.setInternalPort(internalPort);
                    openFireUserEntity.setExternalUrl(externalUrl);
                    openFireUserEntity.setExternalPort(externalPort);
                    isSuccess = true;
                } catch (JSONException e) {
                    LogHelper.d(TAG, "", e);
                }
            }
            return openFireUserEntity;
        }
    }
}
