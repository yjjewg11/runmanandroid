package com.company.runman.asynctask;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;
import com.company.news.vo.UserInfoReturn;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.cache.ImgDownCache;
import com.company.runman.datacenter.model.BaseResultEntity;
import com.company.runman.datacenter.provider.SharePreferenceProvider;
import com.company.runman.net.HttpControl;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.net.request.DefaultRequest;
import com.company.runman.utils.*;
import com.company.runman.widget.DialogUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Administrator on 2015/5/30.
 */
public abstract class UploadImgBitmapAsyncTask extends AbstractAsyncTask<String, Void, Object> {
static String TAG="UploadImgBitmapAsyncTask";
        private Bitmap bitmap;
    private BaseActivity mContext;
    private String url;

        protected UploadImgBitmapAsyncTask(Bitmap bitmap,BaseActivity mContext,String url) {
            this.bitmap = bitmap;
            this.mContext = mContext;
            this.url = url;
        }

        @Override
        protected Object doInBackground(String[] params) {
          //  String url="rest/uploadFile/uploadIdentityCardImg.json";
            DefaultRequest request= new DefaultRequest(mContext,url, Constant.RequestCode.REQUEST_POST);
            HttpReturn httpReturn = HttpControl.execute(bitmap, request.getUrl());

            IResponse response = new IResponse();
            return response.parseFrom(httpReturn);
            //保存是否保存密码的选项
            // return CategoryDataProvider.getInstance().register(mContext, form);
        }

        @Override
        protected void onPostExecute(Object result) {
            try {
            mContext.dismissProgress();
            super.onPostExecute(result);
            if(result == null) {
                DialogUtils.alertErrMsg(mContext, "服务器未知错误!");
            } else {
                BaseResultEntity responseEntity = (BaseResultEntity) result;

                if (Constant.ResponseData.STATUS_SUCCESS.equals(responseEntity.getStatus()) ){

                    JSONObject jsonObject = (JSONObject) responseEntity.getResultObject();

                    onPostExecute2(jsonObject);

                    //   SharePreferenceProvider.getInstance(mContext).setUserinfo(jsonObject.optString(Constant.SharePreferenceKey.Userinfo));
//                    try {
//                        String imgurl=jsonObject.getString("imgurl");
//                        String userinfoStr = SharePreferenceProvider.getInstance(mContext).getUserinfo();
//                        UserInfoReturn user=(UserInfoReturn)new GsonUtils().stringToObject(userinfoStr, UserInfoReturn.class);
//                        user.setIdentity_card_imgurl(imgurl);
//                        userinfoStr=new GsonUtils().toJson(user);
//                        SharePreferenceProvider.getInstance(mContext).setUserinfo(userinfoStr);
//                        if(!TextUtils.isEmpty(imgurl)) {
//                            ImgDownCache.getInstance(mContext).displayImage(Tool.getFullUrl(imgurl), imageView_identity_card_imgurl);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }


                    Toast.makeText(mContext, responseEntity.getResultMsg(), Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(mContext, MyInfoActivity.class);
//                    startActivity(intent);
//                    finish();
                }else{
                    DialogUtils.alertErrMsg(mContext,responseEntity.getResultMsg());
                }
            }

            } catch (Exception e) {
                TraceUtil.traceThrowableLog(e);
                DialogUtils.alertErrMsg(mContext, TAG + "onPostExecute:" + e.getMessage());
            }
        }

    protected abstract void onPostExecute2(JSONObject result);
    }