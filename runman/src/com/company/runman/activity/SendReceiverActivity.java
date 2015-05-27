package com.company.runman.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.utils.LogHelper;

/**
 * Created by Edison on 2014/6/12.
 */
public class SendReceiverActivity extends BaseActivity {

    private static final String TAG = "SendReceiverActivity";
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent it = getIntent();
        if (it != null && it.getAction() != null && it.getAction().equals(Intent.ACTION_SEND)){
            Bundle extras = it.getExtras();
            if (extras.containsKey("android.intent.extra.STREAM")) {
                uri = (Uri) extras.get("android.intent.extra.STREAM");
                LogHelper.i(TAG, "uri:" + uri);
            }
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {

    }
}
