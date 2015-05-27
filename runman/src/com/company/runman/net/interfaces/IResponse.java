package com.company.runman.net.interfaces;

import android.widget.Toast;
import com.company.runman.R;
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

    public Object parseFrom(HttpReturn httpReturn) {
        BaseResultEntity baseResultEntity = new BaseResultEntity();
        if(httpReturn==null){
            baseResultEntity.setStatus(Constant.ResponseData.STATUS_FAIL);
            baseResultEntity.setResultMsg("请求返回异常：HttpReturn is null！");
            return baseResultEntity;
        }
        if ( httpReturn.getCode() == Constant.ResponseCode.TIMEOUT) {
            baseResultEntity.setStatus(Constant.ResponseData.STATUS_FAIL);
            baseResultEntity.setResultMsg("请求超时！");
            return baseResultEntity;
        }else  if ( httpReturn.getCode() == Constant.ResponseCode.UNKNOWN_HOST) {
            baseResultEntity.setStatus(Constant.ResponseData.STATUS_FAIL);
            baseResultEntity.setResultMsg("请求异常：连接不到服务器");
            return baseResultEntity;
        }else  if ( httpReturn.getCode() == Constant.ResponseCode.NOT_CONNECTION) {
            baseResultEntity.setStatus(Constant.ResponseData.STATUS_FAIL);
            baseResultEntity.setResultMsg("请求异常：连接错误");
            return baseResultEntity;
        }else  if ( httpReturn.getCode() == Constant.ResponseCode.URL_ERROR) {
            baseResultEntity.setStatus(Constant.ResponseData.STATUS_FAIL);
            baseResultEntity.setResultMsg("请求异常：地址错误");
            return baseResultEntity;
        }




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
                    baseResultEntity.setResultObject(jsonObject);
                } catch (JSONException e) {
                    LogHelper.e(TAG, "", e);
                    LogHelper.d(TAG,  data.toString());
                    TraceUtil.traceLog(TAG+":"+data);
                    TraceUtil.traceThrowableLog(e);
                    baseResultEntity.setResultMsg(data.toString());
                }
            }
            return baseResultEntity;
        } else {
            String result = "ErrorCode:" + httpReturn.code;
            baseResultEntity.setResultMsg(result);
            baseResultEntity.setResultCode(httpReturn.code);
            baseResultEntity.setStatus(Constant.ResponseData.STATUS_FAIL);
            LogHelper.d(TAG,  baseResultEntity.getResultMsg());

            return baseResultEntity;
        }
    }
}
