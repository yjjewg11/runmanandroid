package com.company.runman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaAuthorizationListener;
import com.company.runman.R;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.utils.LogHelper;

/**
 * Created by Edison on 14-7-22.
 */
public class FrontSDKActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "FrontSDKActivity";
    private FrontiaAuthorization mAuthorization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        initViews();
    }

    @Override
    public void initData() {
        mAuthorization = Frontia.getAuthorization();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.front_sdk_layout);
    }

    private void initViews() {
        Button sinaweiboButton = (Button) findViewById(R.id.sinaBtn);
        sinaweiboButton.setOnClickListener(this);

        Button sinaweiboCancelButton = (Button) findViewById(R.id.sinaQuitBtn);
        sinaweiboCancelButton.setOnClickListener(this);

        Button sinaweiboStatusButton = (Button) findViewById(R.id.sinaStatusBtn);
        sinaweiboStatusButton.setOnClickListener(this);

        Button sinaweiboUserInfoButton = (Button) findViewById(R.id.sinaInfoBtn);
        sinaweiboUserInfoButton.setOnClickListener(this);

        Button qqweiboButton = (Button) findViewById(R.id.tecentBtn);
        qqweiboButton.setOnClickListener(this);

        Button qqweiboCancelButton = (Button) findViewById(R.id.tecentQuitBtn);
        qqweiboCancelButton.setOnClickListener(this);

        Button qqweiboStatusButton = (Button) findViewById(R.id.tecentStatusBtn);
        qqweiboStatusButton.setOnClickListener(this);

        Button qqweiboUserInfoButton = (Button) findViewById(R.id.tecentInfoBtn);
        qqweiboUserInfoButton.setOnClickListener(this);

        Button qqzoneButton = (Button) findViewById(R.id.qqzoneBtn);
        qqzoneButton.setOnClickListener(this);

        Button qqzoneCancelButton = (Button) findViewById(R.id.qqzoneQuitBtn);
        qqzoneCancelButton.setOnClickListener(this);

        Button qqzoneStatusButton = (Button) findViewById(R.id.qqzoneStatusBtn);
        qqzoneStatusButton.setOnClickListener(this);

        Button qqzoneUserInfoButton = (Button) findViewById(R.id.qqzoneInfoBtn);
        qqzoneUserInfoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sinaBtn:
                startSinaWeiboLogin();
                break;
            case R.id.sinaQuitBtn:
                startSinaWeiboLogout();
                break;
            case R.id.sinaStatusBtn:
                startSinaWeiboStatus();
                break;
            case R.id.sinaInfoBtn:
                startSinaWeiboUserInfo();
                break;
            case R.id.tecentBtn:
                startQQWeibo();
                break;
            case R.id.tecentQuitBtn:
                startQQWeiboLogout();
                break;
            case R.id.tecentStatusBtn:
                startQQWeiboStatus();
                break;
            case R.id.tecentInfoBtn:
                startQQWeiboUserInfo();
                break;
            case R.id.qqzoneBtn:
                startQQZone();
                break;
            case R.id.qqzoneQuitBtn:
                startQQZoneLogout();
                break;
            case R.id.qqzoneStatusBtn:
                startQQZoneStatus();
                break;
            case R.id.qqzoneInfoBtn:
                startQQZoneUserInfo();
                break;
        }
    }

    private void startSinaWeiboLogin() {
        //mAuthorization.enableSSO(FrontiaAuthorization.MediaType.SINAWEIBO.toString(), Conf.SINA_APP_KEY);
        mAuthorization.authorize(this,
                FrontiaAuthorization.MediaType.SINAWEIBO.toString(),
                new FrontiaAuthorizationListener.AuthorizationListener() {

                    @Override
                    public void onSuccess(FrontiaUser result) {
                        Frontia.setCurrentAccount(result);

                        String log = "social id: " + result.getId() + "\n"
                                + "token: " + result.getAccessToken() + "\n"
                                + "expired: " + result.getExpiresIn();

                        Toast.makeText(mContext, log, Toast.LENGTH_SHORT).show();
                        LogHelper.d(TAG, log);
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMessage) {
                        Toast.makeText(mContext, "errCode:" + errorCode
                                + ", errMsg:" + errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(mContext, "cancel", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void startSinaWeiboLogout() {
        boolean result = mAuthorization.clearAuthorizationInfo(
                FrontiaAuthorization.MediaType.SINAWEIBO.toString());
        if (result) {
            Frontia.setCurrentAccount(null);
            Toast.makeText(mContext, "新浪微博退出成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "新浪微博退出失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void startQQWeibo() {
        mAuthorization.authorize(this, FrontiaAuthorization.MediaType.QQWEIBO.toString(),
                new FrontiaAuthorizationListener.AuthorizationListener() {

                    @Override
                    public void onSuccess(FrontiaUser result) {
                        Frontia.setCurrentAccount(result);

                        String log = "social id: " + result.getId() + "\n"
                                + "token: " + result.getAccessToken() + "\n"
                                + "expired: " + result.getExpiresIn();

                        Toast.makeText(mContext, log, Toast.LENGTH_SHORT).show();
                        LogHelper.d(TAG, log);
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMessage) {
                        Toast.makeText(mContext, "errCode:" + errorCode
                                + ", errMsg:" + errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(mContext, "cancel", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void startQQWeiboLogout() {
        boolean result = mAuthorization.clearAuthorizationInfo(
                FrontiaAuthorization.MediaType.QQWEIBO.toString());
        if (result) {
            Frontia.setCurrentAccount(null);
            Toast.makeText(mContext, "qq微博退出成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "qq微博退出失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void startQQZone() {
        mAuthorization.authorize(this, FrontiaAuthorization.MediaType.QZONE.toString(),
                new FrontiaAuthorizationListener.AuthorizationListener() {

                    @Override
                    public void onSuccess(FrontiaUser result) {
                        Frontia.setCurrentAccount(result);
                        String log = "social id: " + result.getId() + "\n"
                                + "token: " + result.getAccessToken() + "\n"
                                + "expired: " + result.getExpiresIn();

                        Toast.makeText(mContext, log, Toast.LENGTH_SHORT).show();
                        LogHelper.d(TAG, log);
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMessage) {
                        Toast.makeText(mContext, "errCode:" + errorCode
                                + ", errMsg:" + errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(mContext, "cancel", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void startQQZoneLogout() {
        boolean result = mAuthorization.clearAuthorizationInfo(
                FrontiaAuthorization.MediaType.QZONE.toString());
        if (result) {
            Frontia.setCurrentAccount(null);
            Toast.makeText(mContext, "qq空间退出成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "qq空间退出失败", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startQQZoneStatus() {
        boolean result = mAuthorization.isAuthorizationReady(FrontiaAuthorization.MediaType.QZONE.toString());
        if (result) {
            Toast.makeText(mContext, "已经登录QQ空间帐号", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "未登录QQ空间帐号", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startQQWeiboStatus() {
        boolean result = mAuthorization.isAuthorizationReady(FrontiaAuthorization.MediaType.QQWEIBO.toString());
        if (result) {
            Toast.makeText(mContext, "已经登录QQ微博帐号", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "未登录QQ微博帐号", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startSinaWeiboStatus() {
        boolean result = mAuthorization.isAuthorizationReady(FrontiaAuthorization.MediaType.SINAWEIBO.toString());
        if (result) {
            Toast.makeText(mContext, "已经登录新浪微博帐号", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "未登录新浪微博帐号", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startQQZoneUserInfo() {
        userinfo(FrontiaAuthorization.MediaType.QZONE.toString());

    }

    protected void startQQWeiboUserInfo() {
        userinfo(FrontiaAuthorization.MediaType.QQWEIBO.toString());

    }

    protected void startSinaWeiboUserInfo() {
        userinfo(FrontiaAuthorization.MediaType.SINAWEIBO.toString());

    }

    private void userinfo(String accessToken) {
        mAuthorization.getUserInfo(accessToken, new FrontiaAuthorizationListener.UserInfoListener() {

            @Override
            public void onSuccess(FrontiaUser.FrontiaUserDetail result) {
                String resultStr = "username:" + result.getName() + "\n"
                        + "birthday:" + result.getBirthday() + "\n"
                        + "city:" + result.getCity() + "\n"
                        + "province:" + result.getProvince() + "\n"
                        + "sex:" + result.getSex() + "\n"
                        + "pic url:" + result.getHeadUrl() + "\n";
                Toast.makeText(mContext, resultStr, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                Toast.makeText(mContext, "errCode:" + errCode
                        + ", errMsg:" + errMsg, Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mAuthorization) {
            mAuthorization.onActivityResult(requestCode, resultCode, data);
        }
    }
}
