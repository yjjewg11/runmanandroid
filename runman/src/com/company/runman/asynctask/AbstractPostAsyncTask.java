package com.company.runman.asynctask;

import android.util.Log;
import android.widget.Toast;
import com.company.news.jsonform.AbstractJsonform;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.datacenter.model.BaseResultEntity;
import com.company.runman.net.HttpControl;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.net.request.DefaultRequest;
import com.company.runman.utils.AbstractAsyncTask;
import com.company.runman.utils.Constant;
import com.company.runman.utils.TraceUtil;
import com.company.runman.widget.DialogUtils;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/5/28.
 */
public abstract class AbstractPostAsyncTask extends AbstractAsyncTask<String, Void, Object> {
    static String TAG="AbstractPostAsyncTask";
    private AbstractJsonform form;
    private BaseActivity mContext;
    private String saveurl;
    //  String url="rest/trainingCourse/save.json";
    protected AbstractPostAsyncTask(AbstractJsonform form, BaseActivity mContext, String saveurl) {
        this.form = form;
        this. mContext=mContext;
        this.saveurl=saveurl;
    }


    @Override
    protected Object doInBackground(String[] params) {
        DefaultRequest request= new DefaultRequest(mContext,this.saveurl, Constant.RequestCode.REQUEST_POST);
        request.setData(form);
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