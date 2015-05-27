package com.company.runman.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.company.runman.R;
import com.company.runman.activity.Adapter.BaseListAdapter;

/**
 * Created by EdisonZhao on 14-9-11.
 * Email:zhaoliangyu@sobey.com
 */
public class CommonListView extends RelativeLayout {

    private ListView mListView; //列表控件
    private View mLoadingView; //加载动画
    private View mEmptyTip; //空数据提示
    private View mEmptyTranslucent; //空数据显示半透明

    public CommonListView(Context context) {
        super(context);
        initView(context);
    }

    public CommonListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CommonListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.common_listview_layout, this, true);
        mListView = (ListView) findViewById(R.id.listView);
        mLoadingView = findViewById(R.id.loading_page);
        mEmptyTip = findViewById(R.id.emptyTip);
        mEmptyTranslucent = findViewById(R.id.emptyTranslucent);
    }

    public void showListView() {
        mLoadingView.setVisibility(GONE);
        if (mListView.getAdapter().getCount() > 0) {
            mListView.setVisibility(VISIBLE);
            mEmptyTip.setVisibility(GONE);
        } else {
            mListView.setVisibility(GONE);
            mEmptyTip.setVisibility(VISIBLE);
        }
    }

    public void showListViewTranslucent() {
        mLoadingView.setVisibility(GONE);
        if (mListView.getAdapter().getCount() > 0) {
            mListView.setVisibility(VISIBLE);
            mEmptyTranslucent.setVisibility(GONE);
        } else {
            mListView.setVisibility(GONE);
            mEmptyTranslucent.setVisibility(VISIBLE);
        }
    }

    public void reLoading() {
        mLoadingView.setVisibility(VISIBLE);
        mListView.setVisibility(GONE);
        mEmptyTip.setVisibility(GONE);
    }

    public void setAdapter(BaseListAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    public ListView getListView() {
        return mListView;
    }
}
