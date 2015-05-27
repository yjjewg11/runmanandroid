package com.company.runman.frontia;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaAuthorizationListener;
import com.company.runman.R;
import com.company.runman.utils.LogHelper;

import static com.baidu.frontia.api.FrontiaAuthorization.MediaType.QQWEIBO;
import static com.baidu.frontia.api.FrontiaAuthorization.MediaType.SINAWEIBO;

/**
 * Created by EdisonZhao on 14-7-24.
 * Email:zhaoliangyu@sobey.com
 */
public class FrontiaSdkControl {

    private static final String TAG = "FrontiaSdkControl";
    private static FrontiaSdkControl frontiaSdkControl;
    private static FrontiaAuthorization mAuthorization;

    private FrontiaSdkControl() {
    }

    public static FrontiaSdkControl getInstance(FrontiaAuthorization authorization) {
        if (frontiaSdkControl == null) {
            frontiaSdkControl = new FrontiaSdkControl();
        }
        mAuthorization = authorization;
        return frontiaSdkControl;
    }

    public void startAccountLogin(final Context context, final View view, final FrontiaAuthorization.MediaType type) {
        mAuthorization.authorize((Activity) context, type.toString(),
                new FrontiaAuthorizationListener.AuthorizationListener() {

                    @Override
                    public void onSuccess(FrontiaUser result) {
                        Frontia.setCurrentAccount(result);
                        LogHelper.d(TAG, type + "login success!");
                        if (view instanceof TextView) {
                            TextView textView = (TextView) view;
                            textView.setText(R.string.setting_unbinding);
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.account_unbind_icon, 0, 0, 0);
                        }
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMessage) {
                        LogHelper.d(TAG, type + "login failed! " + errorMessage);
                    }

                    @Override
                    public void onCancel() {
                        LogHelper.d(TAG, type + "login cancel!");
                    }

                });
    }

    public void accountLogout(View view, FrontiaAuthorization.MediaType type) {
        boolean result = mAuthorization.clearAuthorizationInfo(type.toString());
        if (result) {
            Frontia.setCurrentAccount(null);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setText(R.string.setting_binding);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.account_bind_icon, 0, 0, 0);
            }
            LogHelper.d(TAG, type + " logout success!");
        } else {
            LogHelper.d(TAG, type + " logout failed!");
        }
    }


    /**
     * 获得账户绑定状态
     *
     * @param type 账户类型
     * @return 是否已经登录
     */
    public boolean getStatus(FrontiaAuthorization.MediaType type) {
        return mAuthorization.isAuthorizationReady(type.toString());
    }

    /**
     * 获得账户绑定状态
     *
     * @param view 账户view
     * @param type 账户类型
     * @return 是否已经登录
     */
    public void checkStatus(View view, FrontiaAuthorization.MediaType type) {
        boolean result = mAuthorization.isAuthorizationReady(type.toString());
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            if (result) {
                LogHelper.d(TAG, type + " is login!");
                textView.setText(R.string.setting_unbinding);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.account_unbind_icon, 0, 0, 0);
            } else {
                LogHelper.d(TAG, type + " is not login!");
                textView.setText(R.string.setting_binding);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.account_bind_icon, 0, 0, 0);
            }
        }
    }

}
