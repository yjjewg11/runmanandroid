package com.company.runman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.company.news.form.UserLoginForm;
import com.company.news.vo.TrainingPlanVO;
import com.company.runman.R;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.datacenter.model.BaseResultEntity;
import com.company.runman.datacenter.provider.CategoryDataProvider;
import com.company.runman.datacenter.provider.HttpDataProvider;
import com.company.runman.datacenter.provider.SharePreferenceProvider;
import com.company.runman.net.HttpControl;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.net.request.DefaultRequest;
import com.company.runman.net.request.LoginRequest;
import com.company.runman.utils.*;
import com.company.runman.widget.DialogUtils;
import com.company.runman.widget.MyProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Edison on 2014/6/4.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG ="LoginActivity";
    private TextView mLogin;
    private View mRegister, mAccountDelete, mPasswordDelete;
    private EditText mUsername;
    private EditText mPassword;
    private CheckBox checkBox;
    private UserLoginForm account;

    private boolean isInit = true;
    private boolean isSavedPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isInit && isSavedPassword && !TextUtils.isEmpty(mUsername.getText())
                && !TextUtils.isEmpty(mPassword.getText())) {
            //自动登陆
         //   login();
          //  isInit = false;
        }
    }

    @Override
    public void initView() {
        mLogin = (TextView) findViewById(R.id.login_btn);
        mRegister = findViewById(R.id.register_btn);
        mUsername = (EditText) findViewById(R.id.loginname);
        mPassword = (EditText) findViewById(R.id.password);
        checkBox = (CheckBox) findViewById(R.id.isSavePassword);
        mAccountDelete = findViewById(R.id.account_delete);
        mPasswordDelete = findViewById(R.id.password_delete);
        setListener();
    }

    @Override
    public void initData() {
        isSavedPassword = SharePreferenceProvider.getInstance(mContext).getPasswordselector();
        String accountName = SharePreferenceProvider.getInstance(mContext).getAccountName();
        if (!TextUtils.isEmpty(accountName)) {
            mUsername.setText(accountName);
            if(isSavedPassword) {
                String password = SharePreferenceProvider.getInstance(mContext).getPassword(accountName);
                mPassword.setText(password);
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.login_layout);
    }

    private void setListener() {
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mAccountDelete.setOnClickListener(this);
        mPasswordDelete.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharePreferenceProvider.getInstance(mContext).savePasswordSelector(isChecked);
            }
        });
        mUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    mAccountDelete.setVisibility(View.VISIBLE);
                } else {
                    mAccountDelete.setVisibility(View.INVISIBLE);
                }
            }
        });

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    mPasswordDelete.setVisibility(View.VISIBLE);
                } else {
                    mPasswordDelete.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                login();
                break;
            case R.id.register_btn:
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.account_delete:
                mUsername.setText("");
                break;
            case R.id.password_delete:
                mPassword.setText("");
                break;
        }
    }

    /**
     * 开始登陆
     */
    private void login() {
        String usernameStr = mUsername.getText().toString();
        String passwordStr = mPassword.getText().toString();
        if (TextUtils.isEmpty(usernameStr)) {
            Toast.makeText(this, R.string.login_username_input_empty_error, Toast.LENGTH_SHORT).show();
            return;
        } else if(TextUtils.isEmpty(passwordStr)) {
            Toast.makeText(this, R.string.login_password_input_empty_error, Toast.LENGTH_SHORT).show();
            return;
        } else if(Tool.isChinese(usernameStr)) {
            Toast.makeText(this, R.string.login_username_input_error, Toast.LENGTH_SHORT).show();
            return;
        } else if(Tool.isChinese(passwordStr)) {
            Toast.makeText(this, R.string.login_password_input_error, Toast.LENGTH_SHORT).show();
            return;
        } else if (!Tool.isConnection(this)) {
            Toast.makeText(this, R.string.login_no_network, Toast.LENGTH_SHORT).show();
            return;
        }
        account = new UserLoginForm();
        account.setLoginname(usernameStr);
        account.setPassword(passwordStr);
      //  loginAndChangeScreenUnClickable();
        //开始登录
        showProgress("正在登录...");
        new LoginAsyncTask().execute("");
    }

    //登录中
    private void loginAndChangeScreenUnClickable() {
        mLogin.setFocusable(false);
        mLogin.setClickable(false);
        mLogin.setText(R.string.login_processing);
    }

    //登录成功
    private void loginSuccessAndChangeScreenClickable() {
        mLogin.setFocusable(true);
        mLogin.setClickable(true);
        Intent intent = new Intent(mContext, MyInfoActivity.class);
        startActivity(intent);
        finish();
    }

    //登录失败
    private void loginFailAndChangeScreenClickable(BaseResultEntity resultEntity,String msg) {
        mLogin.setFocusable(true);
        mLogin.setClickable(true);
        //恢复按钮文字为登录
        mLogin.setText(R.string.login_btn_text);
        if(resultEntity!=null&&resultEntity.getResultCode() == Constant.ResponseCode.TIMEOUT) {
            Toast.makeText(mContext, R.string.time_out_error, Toast.LENGTH_SHORT).show();
        } else {
            if(resultEntity.getResultMsg()!=null)msg=resultEntity.getResultMsg();
            DialogUtils.alertErrMsg(mContext, resultEntity.getResultMsg());
        }
    }

    @Override
    public void finish() {
        super.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private class LoginAsyncTask extends AbstractAsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String[] params) {
            //是否保存密码
            SharePreferenceProvider.getInstance(mContext).savePasswordSelector(checkBox.isChecked());
            //保存用户登陆名
            SharePreferenceProvider.getInstance(mContext).saveAccountName(account.getLoginname());
            String url="rest/userinfo/login.json";
            DefaultRequest request= new DefaultRequest(mContext,url,Constant.RequestCode.REQUEST_POST);
            request.setParaObj(account);
            HttpReturn httpReturn = HttpControl.execute(request, mContext);
            dismissProgress();
            IResponse response = new IResponse();
            return response.parseFrom(httpReturn);
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
                JSONObject jsonObject = (JSONObject) resultEntity.getResultObject();

                SharePreferenceProvider.getInstance(mContext).setJSESSIONID(jsonObject.optString(Constant.SharePreferenceKey.KEY_JSESSIONID));
                SharePreferenceProvider.getInstance(mContext).setUserinfo(jsonObject.optString(Constant.SharePreferenceKey.Userinfo));
                if(checkBox.isChecked()) {
                    //保存用户密码
                    SharePreferenceProvider.getInstance(mContext).savePassword(account.getLoginname(), account.getPassword());
                }

                Intent intent = new Intent(mContext, MyInfoActivity.class);
                startActivity(intent);
                finish();
//                loginSuccessAndChangeScreenClickable();
            } catch (Exception e) {
                TraceUtil.traceThrowableLog(e);
                DialogUtils.alertErrMsg(mContext,TAG+ "onPostExecute:" + e.getMessage());
            }
        }
    }
}
