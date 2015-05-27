package com.company.runman.activity.base;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import com.company.runman.R;
import com.company.runman.comman.application.SobeyApplication;

/**
 * Created by Edison on 2014/6/5.
 */
public class BaseWebActivity extends BaseActivity {

    public WebView mWebView;
    public View mLoadingPage;
    public View mHeaderMoreView;
    public SobeyApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mWebView = (WebView) findViewById(R.id.webView_content);
        mLoadingPage = findViewById(R.id.loading_page);
        mHeaderMoreView = findViewById(R.id.headerMore);
        mHeaderMoreView.setOnClickListener(this);
        setNeedHomePopupWindow(true);
    }

    @Override
    public void initData() {
        mApplication = (SobeyApplication) getApplication();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.webview_layout);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
