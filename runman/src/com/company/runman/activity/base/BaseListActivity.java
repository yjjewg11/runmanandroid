package com.company.runman.activity.base;

import android.widget.ListView;
import com.company.runman.R;
import com.company.runman.activity.Adapter.BaseListAdapter;
import com.company.runman.comman.application.SobeyApplication;
import com.company.runman.view.CommonListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EdisonZhao on 14-8-8.
 * Email:zhaoliangyu@sobey.com
 */
public abstract class BaseListActivity extends BaseActivity {
    // 当前页码
    protected int pageIndex = 1;
    protected static final String TAG = "BaseListView";
    protected CommonListView mCommonListView;
    protected ListView listView;
    protected BaseListAdapter baseListAdapter;
    protected SobeyApplication mApplication;

    @Override
    public void initView() {
        mCommonListView = (CommonListView) findViewById(R.id.commonListView);
    }

    @Override
    public void initData() {
        mApplication = (SobeyApplication) getApplication();
        doAsyncTask();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setContentView() {

    }

    public abstract void doAsyncTask();


}
