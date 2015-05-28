package com.company.runman.asynctask;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.company.news.jsonform.AbstractJsonform;
import com.company.news.vo.TimeScheduleRelationVO;
import com.company.news.vo.TrainingCourseVO;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.datacenter.model.BaseResultEntity;
import com.company.runman.net.HttpControl;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.net.request.DefaultRequest;
import com.company.runman.utils.AbstractAsyncTask;
import com.company.runman.utils.Constant;
import com.company.runman.utils.GsonUtils;
import com.company.runman.utils.TraceUtil;
import com.company.runman.widget.DialogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/28.
 */
public abstract class AbstractDetailAsyncTask extends AbstractAsyncTask<String, Void, Object> {
    static String TAG="AbstractSaveAsyncTask";
    private Object id;
    private BaseActivity mContext;
    private String geturl;
//    String url = "rest/trainingCourse/{uuid}.json";
//    url = url.replace("{uuid}", id);
    protected AbstractDetailAsyncTask(Object id, BaseActivity mContext,String geturl) {
        this.id = id;
        this. mContext=mContext;
        this.geturl=geturl;
    }

    @Override
    protected Object doInBackground(String[] params) {
        String  url = geturl.replace("{uuid}", id.toString());
        DefaultRequest request= new DefaultRequest(mContext,url, Constant.RequestCode.REQUEST_POST);
//        request.setData(form);
        HttpReturn httpReturn = HttpControl.execute(request, mContext);
        IResponse response = new IResponse();
        return response.parseFrom(httpReturn);
        //保存是否保存密码的选项
        // return CategoryDataProvider.getInstance().register(mContext, form);
    }

    @Override
    protected void onPostExecute(Object result) {
        try{
            mContext.dismissProgress();
            super.onPostExecute(result);
            if(result == null) {
                DialogUtils.alertErrMsg(mContext, "服务器未知错误!");
            } else {
                BaseResultEntity responseEntity = (BaseResultEntity) result;
                if (Constant.ResponseData.STATUS_SUCCESS.equals(responseEntity.getStatus()) ){
                    Toast.makeText(mContext, responseEntity.getResultMsg(), Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = (JSONObject) responseEntity.getResultObject();
//                    String data_id = jsonObject.optString(Constant.ResponseData.DATA_ID);
                    onPostExecute2(jsonObject);
                }else{
                    DialogUtils.alertErrMsg(mContext,responseEntity.getResultMsg());
                }
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            TraceUtil.traceThrowableLog(e);
            DialogUtils.alertErrMsg(mContext,e.getMessage());
            DialogUtils.alertErrMsg(mContext,TAG+ " onPostExecute:" + e.getMessage());
        }
    }
    protected abstract void onPostExecute2(JSONObject result);
}
