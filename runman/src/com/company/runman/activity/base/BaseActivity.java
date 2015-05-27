package com.company.runman.activity.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mobstat.StatService;
import com.company.runman.R;
import com.company.runman.datacenter.model.UpgradeRequestEntity;
import com.company.runman.datacenter.model.UpgradeResponseEntity;
import com.company.runman.datacenter.provider.CategoryDataProvider;
import com.company.runman.datacenter.provider.SharePreferenceProvider;
import com.company.runman.utils.*;
import com.company.runman.view.HeaderMoreView;
import com.company.runman.widget.MyProgressDialog;

/**
 * Created by Edison on 2014/6/4.
 * Email:zhaoliangyu@sobey.com
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener{
    protected static final String TAG = "BaseActivity";
    public Context mContext;
    private MyProgressDialog progressDialog;
    private boolean isNeedReturnHomePopupWindow = false;
    private boolean isNeedScanHistoryBtn = false;
    private boolean isNeedMonitorEditBtn = false;
    private View.OnClickListener monitorEditListener;
    protected PopupWindow mPopupWindow;
    //private ExitBroadcastReceiver exitBroadcastReceiver = new ExitBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            mContext = this;
            if(SharePreferenceProvider.g_context==null)SharePreferenceProvider.g_context=this;
            progressDialog = new MyProgressDialog(mContext);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            setContentView();

            initView();
            initData();
            //退出程序广播
          //  registerReceiver(exitBroadcastReceiver, new IntentFilter(Constant.Action.EXIT_ACTION));
        }catch(Exception e){
            TraceUtil.traceThrowableLog(e);
            e.printStackTrace();
        }

    }

    public void showProgress(String message) {
        progressDialog.setMessage(message);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void showDefaultProgress() {
        progressDialog.setMessage(getString(R.string.loading_text));
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
//        if(isNeedReturnHomePopupWindow) {
//            setHeaderMoreVisibleAndClickListener();
//        }
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        mPopupWindow = null;
      //  unregisterReceiver(exitBroadcastReceiver);
        super.onDestroy();
    }

    public abstract void initView();

    public abstract void initData();

    public abstract void setContentView();

    @Override
    public void onClick(View v) {}

    /**
     * 显示返回按钮（返回按钮和侧边栏菜单按钮是互斥的）
     */
    public void setHeaderBackVisible() {
        findViewById(R.id.headerBack).setVisibility(View.GONE);
        findViewById(R.id.headerMenu).setVisibility(View.VISIBLE);
    }

//    /**
//     * 显示更多菜单按钮（更多菜单和搜索按钮是互斥的）
//     */
//    private void setHeaderMoreVisibleAndClickListener() {
//        View headerMore = findViewById(R.id.headerMore);
//        headerMore.setVisibility(View.VISIBLE);
//        headerMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupWindow(v);
//            }
//        });
//    }

    /**
     * 是否需要显示返回到首页的右上角菜单window
     * @param value
     */
    protected void setNeedHomePopupWindow(boolean value) {
        isNeedReturnHomePopupWindow = value;
    }

    protected void setNeedScanHistoryBtn(boolean value) {
        isNeedScanHistoryBtn = value;
    }

    protected void setNeedMonitorEditBtn(boolean value, View.OnClickListener onClickListener) {
        isNeedMonitorEditBtn = value;
        monitorEditListener = onClickListener;
    }

    /**
     * 修改标题
     * @param title
     */
    public void setHeaderTitle(String title) {
        ((TextView)findViewById(R.id.headerTitle)).setText(title);
    }

    public void setHeaderTitle(int titleId) {
        setHeaderTitle(getString(titleId));
    }

    public TextView getTitleView() {
        return (TextView)findViewById(R.id.headerTitle);
    }

    /**
     * 请求版本更新
     */
    public void checkVersion(CheckUpdateCallBack callBack) {
      //  new BaseActivity.CheckUpgradeAsyncTask(callBack).execute("");
    }

    /**
     * 调用注销接口,并且退出
     */
    public void loginOut(){
        LogHelper.d(TAG, "loginOut");
        //调用注销接口
        new LoginOutAsyncTask().execute("");
        //停止服务
      //  stopService(new Intent(mContext, MessageService.class));
        //停止程序
        sendBroadcast(new Intent(Constant.Action.EXIT_ACTION));
    }


    class ExitBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(TextUtils.equals(intent.getAction(), Constant.Action.EXIT_ACTION)) {
                //finish();
            }
        }
    }

    /**
     * 注销线程
     */
    private class LoginOutAsyncTask extends AbstractAsyncTask<String, Void, Object> {

        private LoginOutAsyncTask() {}

        @Override
        protected Object doInBackground(String[] params) {
            return CategoryDataProvider.getInstance().loginOut(mContext);
        }

        @Override
        protected void onPostExecute(Object accountEntity) {
            super.onPostExecute(accountEntity);
        }
    }

    //检查网络
    public boolean checkNetwork() {
        if(Tool.isConnection(mContext)) {
            return true;
        } else {
            Toast.makeText(mContext, R.string.network_offline, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public interface CheckUpdateCallBack{
        public void callBack(boolean isNewVersion, UpgradeResponseEntity responseEntity);
    }

    public final String getStringAfterCheck(int resId, Object... formatArgs) {
        if(formatArgs != null) {
            for(int i=0;i<formatArgs.length;i++) {
                Object object = formatArgs[i];
                if(object == null || "null".equals(object)) {
                    formatArgs[i] = "";
                }
            }
        }
        return getResources().getString(resId, formatArgs);
    }


    public void showPopupWindow(View v) {
        if (null == mPopupWindow) {
            HeaderMoreView popupView = new HeaderMoreView(mContext);
            mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
            popupView.setParentWindow(mPopupWindow);
            if(isNeedMonitorEditBtn) {
                popupView.getMonitorEditView().setVisibility(View.VISIBLE);
                popupView.getMonitorEditView().setOnClickListener(monitorEditListener);
            }
            if(isNeedScanHistoryBtn) {
                popupView.getScanHistory().setVisibility(View.VISIBLE);
            }
        }
        mPopupWindow.showAsDropDown(v);
    }
}
