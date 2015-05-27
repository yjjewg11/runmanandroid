package com.company.runman.net.request;

import android.content.Context;
import com.company.runman.datacenter.model.UpgradeRequestEntity;
import com.company.runman.datacenter.model.UpgradeResponseEntity;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IRequest;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.utils.Constant;
import com.company.runman.utils.LogHelper;
import org.json.JSONObject;

/**
 * Created by EdisonZhao on 14-8-11.
 * Email:zhaoliangyu@sobey.com
 */
public class CheckUpgradeRequest extends IRequest {

    private UpgradeRequestEntity upgradeRequestEntity;

    public CheckUpgradeRequest(Context context) {
        super(context);
    }

    @Override
    public void setData(Object T) {
        if (T instanceof UpgradeRequestEntity) {
            upgradeRequestEntity = (UpgradeRequestEntity) T;
        }
    }

    @Override
    public String getUrl() {
        StringBuffer sb = new StringBuffer();
        String api = "rest/upgrade.json";
        return sb.append(Constant.Host.BASE_HOST).append(api)
                .append("?type=android")
                .append("&version=").append(upgradeRequestEntity.getVersionName())
                .append("&appKey=").append(upgradeRequestEntity.getAppKey()).toString();
    }

    @Override
    public String getPost() {
        return null;
    }

    @Override
    public int getHttpMode() {
        return Constant.RequestCode.REQUEST_GET;
    }

    public static final class CheckUpgradeResponse extends IResponse {

        private UpgradeResponseEntity upgradeResponseEntity;

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            if (data != null) {
                try {
                    JSONObject jsonObject = new JSONObject(data.toString());
                    upgradeResponseEntity = new UpgradeResponseEntity();
                    upgradeResponseEntity.setDescription(jsonObject.optString("desc"));
                    upgradeResponseEntity.setDownLink(jsonObject.optString("downLink"));
                    upgradeResponseEntity.setReleaseTime(jsonObject.optString("releaseTime"));
                    upgradeResponseEntity.setSize(jsonObject.optString("size"));
                    upgradeResponseEntity.setVersion(jsonObject.optString("version"));
                    upgradeResponseEntity.setAppid(jsonObject.optString("appid"));
                } catch (Exception e) {
                    LogHelper.d(TAG, "", e);
                }
            }
            return upgradeResponseEntity;
        }
    }
}
