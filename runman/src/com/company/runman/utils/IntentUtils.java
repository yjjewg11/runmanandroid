package com.company.runman.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.company.runman.activity.TrainingPlanDetailActivity;
import com.company.runman.activity.TrainingPlanEditActivity;
import com.company.runman.datacenter.provider.SharePreferenceProvider;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Edison on 2014/6/5.
 */
public class IntentUtils {

    private static final String TAG = "IntentUtils";

    /**
     * 跳转编辑训练计划模块
     * @param mContext
     * @param data
     */
    public static void startTrainingPlanDetailActivity(Context mContext,Serializable data) {
        Intent intent = new Intent(mContext, TrainingPlanDetailActivity.class);
        if(data!=null){
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.ResponseData.DATA,  data);
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
    /**
     * 跳转编辑训练计划模块
     * @param mContext
     * @param data
     */
    public static void startTrainingPlanEditActivity(Context mContext,Serializable data) {
        Intent intent = new Intent(mContext, TrainingPlanEditActivity.class);
        if(data!=null){
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.ResponseData.DATA,  data);
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
    public static Intent getSearchIntent(String keywords) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEARCH);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constant.ExtraKey.EXTRA_KEYWORD, keywords);
        return intent;
    }

    public static Intent getIntentToBrowsable(String uri) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(uri));
        intent.addCategory("android.intent.category.BROWSABLE");
        //intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        return intent;
    }


    /**
     * 同步一下cookie
     */
    public static void synCookies(Context context) {
        CookieSyncManager.createInstance(context);
        ArrayList<String> arrayList = new ArrayList<String>();

        String JSESSIONID = SharePreferenceProvider.getInstance(context).getJSESSIONID();

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(Constant.Host.BASE_HOST, Constant.SharePreferenceKey.KEY_JSESSIONID + "=" + JSESSIONID);

        CookieSyncManager.getInstance().sync();
    }
}
