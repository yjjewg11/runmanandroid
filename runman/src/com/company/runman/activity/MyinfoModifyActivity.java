package com.company.runman.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.company.news.jsonform.TrainingPlanJsonform;
import com.company.news.jsonform.UserModifyJsonform;
import com.company.news.validate.CommonsValidate;
import com.company.news.vo.TrainingPlanVO;
import com.company.news.vo.UserInfoReturn;
import com.company.runman.R;
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
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by LMQ on 14-9-3.
 * Email:
 */
public class MyinfoModifyActivity extends BaseActivity {
    static private String ClassName="MyinfoModifyActivity";
    private UserModifyJsonform form;
    private RadioButton sex_0;
    private RadioButton sex_1;
    private EditText name;
    private EditText real_name;
    private EditText identity_card;
    private EditText sex;
    private EditText city;
    private EditText birth;
    private View button_modify;
    private Button uploadMyheadImg;
    private EditText context;

    private ImageView imageView_myhead;

    private String picPath = null;

    @Override
    public void initView() {
        setHeaderTitle("修改个人资料");
    }

    @Override
    public void initData() {
        Intent intent = this.getIntent();
//        UserInfoReturn vo=(UserInfoReturn)intent.getSerializableExtra(Constant.ResponseData.DATA);
        String userinfoStr=SharePreferenceProvider.getInstance(mContext).getUserinfo();
        UserInfoReturn vo=(UserInfoReturn) new GsonUtils().stringToObject(userinfoStr, UserInfoReturn.class);
        if(vo==null){
            //
            DialogUtils.alertErrMsg(mContext,"异常：客户端用户资料不存在，请重新登录！");
            return;
        }
        if(Integer.valueOf(1).equals(vo.getSex())){
            sex_1.setChecked(true);
        }else{
            sex_0.setChecked(true);
        }
        name.setText(Tool.objectToString(vo.getName()));
        real_name.setText(Tool.objectToString(vo.getReal_name()));
        identity_card.setText(Tool.objectToString(vo.getIdentity_card()));
        city.setText(Tool.objectToString(vo.getCity()));
        birth.setText(Tool.objectToString(TimeUtils.getDateString(vo.getBirth())));
        context.setText(Tool.objectToString(vo.getContext()));
        if(!TextUtils.isEmpty(vo.getHead_imgurl())) {
            ImgDownCache.getInstance(mContext).displayImage(Tool.getFullUrl(vo.getHead_imgurl()), imageView_myhead);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            /**
             * 当选择的图片不为空的话，在获取到图片的途径
             */
            Uri uri = data.getData();
            Log.e(TAG, "uri = " + uri);
            TraceUtil.traceLog(TAG+".uri="+uri);
            try {
                String[] pojo = { MediaStore.Images.Media.DATA };

                Cursor cursor = managedQuery(uri, pojo, null, null, null);
                if (cursor != null) {
                    ContentResolver cr = this.getContentResolver();
                    int colunm_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(colunm_index);
                    /***
                     * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，
                     * 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
                     */
                     TraceUtil.traceLog("upload img path="+path);
//                    if (path.endsWith("jpg") || path.endsWith("png")||path.endsWith("gif")) {
                        picPath = path;

                        final File file = new File(picPath);

                        if (file != null) {
                            showProgress(getString(R.string.progress_text));
                            new UploadMyheadImgAsyncTask(file).execute("");
                        }
                        Bitmap bitmap = BitmapFactory.decodeStream(cr
                                .openInputStream(uri));
                        imageView_myhead.setImageBitmap(bitmap);
//                    }
//                    else {
//                        alert_img_err();
//                    }
                } else {
                    alert_img_err();
                }

            } catch (Exception e) {
                TraceUtil.traceThrowableLog(e);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void alert_img_err() {
        Toast.makeText(mContext,"您选择的不是有效的图片", Toast.LENGTH_SHORT).show();
        picPath = null;
    }
    @Override
    public void setContentView() {
        setContentView(R.layout.myinfo_modify_layout);
        sex_0 = (RadioButton) findViewById(R.id.sex_0);
        sex_1 = (RadioButton) findViewById(R.id.sex_1);

        name = (EditText) findViewById(R.id.name);
        real_name= (EditText) findViewById(R.id.real_name);;
        identity_card= (EditText) findViewById(R.id.identity_card);
        city= (EditText) findViewById(R.id.city);
        birth= (EditText) findViewById(R.id.birth);
        context=(EditText) findViewById(R.id.context);
        DialogUtils.showDateDialogBind(birth);
        imageView_myhead=(ImageView) findViewById(R.id.imageView_myhead);

        button_modify = findViewById(R.id.button_modify);
        button_modify.setOnClickListener(this);
        uploadMyheadImg =(Button) findViewById(R.id.uploadMyheadImg);
        uploadMyheadImg.setOnClickListener(this);

    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == button_modify) {
            if (!Tool.isConnection(mContext)) {
                Toast.makeText(mContext, R.string.network_offline, Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(name.getText())) {
                Toast.makeText(mContext, "昵称不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (form == null) {//创建
                form = new UserModifyJsonform();
            }
            if (sex_1.isChecked()) {
                form.setSex(1);
            } else {
                form.setSex(0);
            }
            try {

                form.setName(name.getText().toString());
                if (sex_1.isChecked()) {
                    form.setSex(1);
                } else {
                    form.setSex(0);
                }
                if (!TextUtils.isEmpty(birth.getText())) {
                    form.setBirth(TimeUtils.getTimestamp(birth.getText().toString(), " 00:00:00"));
                }
                form.setCity(city.getText().toString());
                form.setContext(context.getText().toString());
                form.setIdentity_card(identity_card.getText().toString());
                form.setReal_name(real_name.getText().toString());


                showProgress(getString(R.string.progress_text));
                new MyinfoModifyAsyncTask(form).execute("");
            } catch (Exception e) {
                TraceUtil.traceLog(TAG+"::"+e.getMessage());
            }
        }else if(v ==uploadMyheadImg){
            /***
             * 这个是调用android内置的intent，来过滤图片文件 ，同时也可以过滤其他的
             */

//            Intent intent= new Intent(
//                    Intent.ACTION_PICK,
//                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            intent.setType("image/*");
//            startActivityForResult(intent, 1);

//
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(intent, 1);

            Intent intent;
            intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent(mContext, MyInfoActivity.class);
        startActivity(intent);
        super.finish();
    }

    private class MyinfoModifyAsyncTask extends AbstractAsyncTask<String, Void, Object> {

        private Object form;

        protected MyinfoModifyAsyncTask(Object form) {
            this.form = form;
        }

        @Override
        protected Object doInBackground(String[] params) {
            String url="rest/userinfo/modify.json";
            DefaultRequest request= new DefaultRequest(mContext,url,Constant.RequestCode.REQUEST_POST);
            request.setData(form);
            HttpReturn httpReturn = HttpControl.execute(request, mContext);
            IResponse response = new IResponse();
            return response.parseFrom(httpReturn);
            //保存是否保存密码的选项
            // return CategoryDataProvider.getInstance().register(mContext, form);
        }

        @Override
        protected void onPostExecute(Object result) {
            dismissProgress();
            super.onPostExecute(result);
            if(result == null) {
                DialogUtils.alertErrMsg(mContext,"服务器未知错误!");
            } else {
                BaseResultEntity responseEntity = (BaseResultEntity) result;

                if (Constant.ResponseData.STATUS_SUCCESS.equals(responseEntity.getStatus()) ){

                    JSONObject jsonObject = (JSONObject) responseEntity.getResultObject();
                    SharePreferenceProvider.getInstance(mContext).setUserinfo(jsonObject.optString(Constant.SharePreferenceKey.Userinfo));

                    Toast.makeText(mContext, responseEntity.getResultMsg(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, MyInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userinfo_update", "1");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }else{
                    DialogUtils.alertErrMsg(mContext,responseEntity.getResultMsg());
                }
            }
        }
    }

    private class UploadMyheadImgAsyncTask extends AbstractAsyncTask<String, Void, Object> {

        private File  form;

        protected UploadMyheadImgAsyncTask(File form) {
            this.form = form;
        }

        @Override
        protected Object doInBackground(String[] params) {
            String url="rest/uploadFile/uploadMyheadImg.json";
            DefaultRequest request= new DefaultRequest(mContext,url,Constant.RequestCode.REQUEST_POST);
            HttpReturn httpReturn = HttpControl.execute(form,request.getUrl());

            IResponse response = new IResponse();
            return response.parseFrom(httpReturn);
            //保存是否保存密码的选项
           // return CategoryDataProvider.getInstance().register(mContext, form);
        }

        @Override
        protected void onPostExecute(Object result) {
            dismissProgress();
            super.onPostExecute(result);
            if(result == null) {
                DialogUtils.alertErrMsg(mContext,"服务器未知错误!");
            } else {
                BaseResultEntity responseEntity = (BaseResultEntity) result;

                if (Constant.ResponseData.STATUS_SUCCESS.equals(responseEntity.getStatus()) ){

                    JSONObject jsonObject = (JSONObject) responseEntity.getResultObject();
                 //   SharePreferenceProvider.getInstance(mContext).setUserinfo(jsonObject.optString(Constant.SharePreferenceKey.Userinfo));
                    try {
                        String imgurl=jsonObject.getString("imgurl");
                        String userinfoStr = SharePreferenceProvider.getInstance(mContext).getUserinfo();
                        UserInfoReturn user=(UserInfoReturn)new GsonUtils().stringToObject(userinfoStr, UserInfoReturn.class);
                        user.setHead_imgurl(imgurl);
                        userinfoStr=new GsonUtils().toJson(user);
                        SharePreferenceProvider.getInstance(mContext).setUserinfo(userinfoStr);
                        if(!TextUtils.isEmpty(imgurl)) {
                            ImgDownCache.getInstance(mContext).displayImage(Tool.getFullUrl(imgurl), imageView_myhead);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Toast.makeText(mContext, responseEntity.getResultMsg(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, MyInfoActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    DialogUtils.alertErrMsg(mContext,responseEntity.getResultMsg());
                }
            }
        }
    }
}
