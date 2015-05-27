package com.company.runman.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Edison on 2014/6/5.
 */
public class SobeyWebChromeClient extends WebChromeClient {

    private static final String TAG = "SobeyWebChromeClient";
    private Activity mActivity;
    public ValueCallback<Uri> mUploadMessage;
    public final static int FILE_CHOOSER_RESULT_CODE = 1;

    public SobeyWebChromeClient(Activity activity) {
        mActivity = activity;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        LogHelper.v(TAG, "onJsAlert.url=" + url);
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        LogHelper.v(TAG, "onJsConfirm.url=" + url);
        return super.onJsConfirm(view, url, message, result);
    }

    //获得网页标题
    @Override
    public void onReceivedTitle(WebView view, String title) {
        LogHelper.v(TAG, "onReceivedTitle.title=" + title);
        super.onReceivedTitle(view, title);
    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        mActivity.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        mActivity.startActivityForResult(Intent.createChooser(i, "File Browser"), FILE_CHOOSER_RESULT_CODE);
    }

    //For Android 4.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        mActivity.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_CHOOSER_RESULT_CODE);
    }
};
