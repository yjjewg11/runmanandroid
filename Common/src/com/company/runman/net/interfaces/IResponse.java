package com.company.runman.net.interfaces;

import com.company.runman.datacenter.model.BaseResultEntity;
import com.company.runman.datacenter.provider.SharePreferenceProvider;
import com.company.runman.net.HttpReturn;
import com.company.runman.utils.Constant;
import com.company.runman.utils.LogHelper;
import com.company.runman.utils.TraceUtil;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Edison on 2014/6/6.
 */
public  class IResponse {

    public static final String TAG = "Response";

    public boolean isSuccess = false;

    public Object parseFrom(HttpReturn httpReturn) {
        BaseResultEntity baseResultEntity = new BaseResultEntity();
        byte[] bytes = httpReturn.getBytes();
        if (null != bytes && bytes.length != 0) {
            //解析数据
            String data = new String(bytes);
            LogHelper.d(TAG, data);
            TraceUtil.traceLog(TAG+":"+data);
            baseResultEntity.setResultCode(httpReturn.getCode());
            if (data != null) {
                try {
                    JSONObject jsonObject = new JSONObject(data.toString());
                    JSONObject resMsg = jsonObject.optJSONObject(Constant.ResponseData.ResMsg);
                    baseResultEntity.setStatus(resMsg.optString(Constant.ResponseData.STATUS));
                    baseResultEntity.setResultMsg(resMsg.optString(Constant.ResponseData.MESSAGE));
                    if(Constant.ResponseData.STATUS_SUCCESS.equals(baseResultEntity.getStatus())){
                        isSuccess=true;
                    }else{
                        isSuccess=false;
                    }
                } catch (JSONException e) {
                    LogHelper.e(TAG, "", e);
                    LogHelper.d(TAG,  data.toString());
                    baseResultEntity.setResultMsg(data.toString());
                }
                baseResultEntity.setResultObject(data);
            }
            return baseResultEntity;
        } else {
            String result = "ErrorCode:" + httpReturn.code;
            baseResultEntity.setResultMsg(result);
            baseResultEntity.setResultCode(httpReturn.code);
            isSuccess = false;
            LogHelper.d(TAG,  baseResultEntity.getResultMsg());

            return baseResultEntity;
        }
    }
}
