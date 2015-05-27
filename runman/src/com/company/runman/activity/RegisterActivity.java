package com.company.runman.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.company.news.jsonform.UserRegJsonform;
import com.company.news.validate.CommonsValidate;
import com.company.runman.R;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.datacenter.model.AccountEntity;
import com.company.runman.datacenter.model.BaseResultEntity;
import com.company.runman.datacenter.model.RegisterResponseEntity;
import com.company.runman.datacenter.provider.CategoryDataProvider;
import com.company.runman.datacenter.provider.SharePreferenceProvider;
import com.company.runman.net.HttpControl;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.net.request.DefaultRequest;
import com.company.runman.utils.Constant;
import com.company.runman.utils.AbstractAsyncTask;
import com.company.runman.utils.Tool;
import com.company.runman.utils.TraceUtil;
import com.company.runman.widget.DialogUtils;
import com.company.runman.widget.MyProgressDialog;
import org.json.JSONObject;

/**
 * Created by EdisonZhao on 14-9-3.
 * Email:zhaoliangyu@sobey.com
 */
public class RegisterActivity extends BaseActivity {

    private EditText loginname;
    private EditText password;
    private EditText confirmPassword;
    private EditText name;
    private EditText tel_smscode;
    private View sendCode_btn;
    private View registerBtn;

    @Override
    public void initView() {
        setHeaderTitle(R.string.register_title);
    }

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.register_layout);
        loginname = (EditText) findViewById(R.id.loginname);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        name = (EditText) findViewById(R.id.name);
        tel_smscode = (EditText) findViewById(R.id.tel_smscode);

        sendCode_btn = findViewById(R.id.sendCode_btn);
        sendCode_btn.setOnClickListener(this);
        registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == sendCode_btn) {
            if(!Tool.isConnection(mContext)) {
                Toast.makeText(mContext, R.string.network_offline, Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(loginname.getText())){
                Toast.makeText(mContext, "电话不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!CommonsValidate.checkCellphone(loginname.getText().toString())){
                Toast.makeText(mContext, "电话号码不合法", Toast.LENGTH_SHORT).show();
                return;
            }
            sendCode_btn.setFocusable(false);
            sendCode_btn.setClickable(false);
            showProgress("正在发送...");
            new SendSmsCodeAsyncTask().execute("");

        }
        if(v == registerBtn) {
            if(!Tool.isConnection(mContext)) {
                Toast.makeText(mContext, R.string.network_offline, Toast.LENGTH_SHORT).show();
                return;
            } else if(TextUtils.isEmpty(loginname.getText())){
                Toast.makeText(mContext, "电话不能为空!", Toast.LENGTH_SHORT).show();
                return;
            } else if(TextUtils.isEmpty(name.getText())) {
                Toast.makeText(mContext, "昵称不能为空!", Toast.LENGTH_SHORT).show();
                return;
            } else if(TextUtils.isEmpty(tel_smscode.getText())) {
                Toast.makeText(mContext, "验证码不能为空!", Toast.LENGTH_SHORT).show();
                return;
            } else if(TextUtils.isEmpty(password.getText())) {
                Toast.makeText(mContext, "密码不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }else if(password.getText().length() < 6 || password.length() > 14) {
                Toast.makeText(mContext, "密码长度应该为6-14位的字符", Toast.LENGTH_SHORT).show();
                return;
            } else if(!TextUtils.equals(password.getText(), confirmPassword.getText())) {
                Toast.makeText(mContext, "两次密码输入不一致,请重新输入", Toast.LENGTH_SHORT).show();
                return;
            }else if(Tool.isChinese(loginname.getText().toString()) || Tool.isContainsSpecChars(loginname.getText())) {
                Toast.makeText(this, R.string.login_username_input_error, Toast.LENGTH_SHORT).show();
                return;
            } else if(Tool.isChinese(password.getText().toString()) || Tool.isContainsSpecChars(password.getText())) {
                Toast.makeText(this, R.string.login_password_input_error, Toast.LENGTH_SHORT).show();
                return;
            }
            UserRegJsonform form = new UserRegJsonform();
            form.setLoginname(loginname.getText().toString());
            form.setName(name.getText().toString());
            form.setPassword(password.getText().toString());
            form.setTel_smscode(tel_smscode.getText().toString());
            showProgress("正在发送...");
            new LoginAsyncTask(form).execute("");
        }
    }

    @Override
    public void finish() {
        super.finish();
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
    }

    private class SendSmsCodeAsyncTask extends AbstractAsyncTask<String, Void, Object> {


        @Override
        protected Object doInBackground(String[] params) {

            String url="rest/sms/sendCode.json?tel="+loginname.getText().toString();
            DefaultRequest request= new DefaultRequest(mContext,url,Constant.RequestCode.REQUEST_GET);
            HttpReturn httpReturn = HttpControl.execute(request, mContext);
            IResponse response = new IResponse();
            return response.parseFrom(httpReturn);
        }

        @Override
        protected void onPostExecute(Object result) {
            sendCode_btn.setFocusable(true);
            sendCode_btn.setClickable(true);
            try {
                super.onPostExecute(result);
                BaseResultEntity resultEntity = (BaseResultEntity) result;
                if (!Constant.ResponseData.STATUS_SUCCESS.equals(resultEntity.getStatus())) {
                    DialogUtils.alertErrMsg(mContext, resultEntity.getResultMsg());
                    return;
                }
                Toast.makeText(mContext, resultEntity.getResultMsg(), Toast.LENGTH_SHORT).show();
                String accountName = loginname.getText().toString();
                String accountPassword = password.getText().toString();
                SharePreferenceProvider.getInstance(mContext).saveAccountName(accountName);
                boolean isSavePassword = SharePreferenceProvider.getInstance(mContext).getPasswordselector();
                if(isSavePassword) {
                    //如果需要保存密码
                    SharePreferenceProvider.getInstance(mContext).savePassword(accountName, accountPassword);
                }


//                loginSuccessAndChangeScreenClickable();
            } catch (Exception e) {
                TraceUtil.traceThrowableLog(e);
                DialogUtils.alertErrMsg(mContext, "onPostExecute:" + e.getMessage());
            }

        }
    }

    private class LoginAsyncTask extends AbstractAsyncTask<String, Void, Object> {

        private UserRegJsonform form;

        protected LoginAsyncTask(UserRegJsonform accountEntity) {
            this.form = accountEntity;
        }

        @Override
        protected Object doInBackground(String[] params) {
            String url="rest/userinfo/reg.json";
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
            try {
                super.onPostExecute(result);
                BaseResultEntity resultEntity = (BaseResultEntity) result;
                if (!Constant.ResponseData.STATUS_SUCCESS.equals(resultEntity.getStatus())) {
                    DialogUtils.alertErrMsg(mContext, resultEntity.getResultMsg());
                    return;
                }
                Toast.makeText(mContext, resultEntity.getResultMsg(), Toast.LENGTH_SHORT).show();
                String accountName = loginname.getText().toString();
                String accountPassword = password.getText().toString();
                SharePreferenceProvider.getInstance(mContext).saveAccountName(accountName);
                boolean isSavePassword = SharePreferenceProvider.getInstance(mContext).getPasswordselector();
                if(isSavePassword) {
                    //如果需要保存密码
                    SharePreferenceProvider.getInstance(mContext).savePassword(accountName, accountPassword);
                }
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();

//                loginSuccessAndChangeScreenClickable();
            } catch (Exception e) {
                TraceUtil.traceThrowableLog(e);
                DialogUtils.alertErrMsg(mContext, "onPostExecute:" + e.getMessage());
            }
        }
    }
}
