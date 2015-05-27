package com.company.runman.comman.application;

import android.content.Context;
import com.baidu.frontia.Frontia;
import com.company.runman.datacenter.database.DBUtil;
import com.company.runman.datacenter.model.ConfigEntity;
import com.company.runman.datacenter.provider.CategoryDataProvider;
import com.company.runman.datacenter.provider.SharePreferenceProvider;
import com.company.runman.utils.Constant;
import com.company.runman.utils.LogHelper;
import com.company.runman.utils.SobeyHandlerCenter;

/**
 * Created by EdisonZhao on 14-8-5.
 * Email:zhaoliangyu@sobey.com
 */
public class ApplicationInitTask {

    private static final String TAG = "ApplicationInitTask";
    private static ApplicationInitTask applicationInitTask;

    public static ApplicationInitTask getInstance() {
        if (applicationInitTask == null) {
            applicationInitTask = new ApplicationInitTask();
        }
        return applicationInitTask;
    }

    public void initThread(final Context mContext) {
        SobeyHandlerCenter.getBusinessHandler().post(new Runnable() {
            @Override
            public void run() {
                //初始化配置信息
                initConfig(mContext);
                //检查数据库
             //   DBUtil.checkUpgrade(mContext);
                //初始化百度社会化组件
                boolean isInit = Frontia.init(mContext, Constant.BAIDU_API_KEY);
                LogHelper.e(TAG, "baidu share :" + isInit);
            }
        });
    }

    public void initConfig(Context context) {
        try {
        } catch (Exception e) {
            LogHelper.d(TAG, "", e);
        }
    }
}
