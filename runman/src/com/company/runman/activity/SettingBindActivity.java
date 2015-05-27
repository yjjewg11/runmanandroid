package com.company.runman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.company.runman.R;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.frontia.FrontiaSdkControl;
import com.company.runman.utils.Constant;
import com.company.runman.utils.TraceUtil;

import static com.baidu.frontia.api.FrontiaAuthorization.MediaType.*;

/**
 * Created by EdisonZhao on 14-7-24.
 * Email:zhaoliangyu@sobey.com
 */
public class SettingBindActivity extends BaseActivity {

    private static final String TAG = "SettingBindActivity";

    private TextView sinaBindView;
    private TextView tencentBindView;
    private TextView renrenBindView;
    private TextView qqBindView;
    private TextView qzoneBindView;
    private FrontiaAuthorization mAuthorization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setHeaderTitle(R.string.title_binding);
        try {
            Frontia.init(mContext, Constant.BAIDU_API_KEY);
            mAuthorization = Frontia.getAuthorization();
        } catch(NullPointerException e) {
            TraceUtil.traceThrowableLog(e);
        }
        sinaBindView = (TextView) findViewById(R.id.sinaBindBtn);
        tencentBindView = (TextView) findViewById(R.id.tencentBindBtn);
        renrenBindView = (TextView) findViewById(R.id.renrenBindBtn);
        qqBindView = (TextView) findViewById(R.id.qqBindBtn);
        qzoneBindView = (TextView) findViewById(R.id.qzoneBindBtn);
        sinaBindView.setOnClickListener(this);
        tencentBindView.setOnClickListener(this);
        renrenBindView.setOnClickListener(this);
        qqBindView.setOnClickListener(this);
        qzoneBindView.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if(mAuthorization == null) {
            Toast.makeText(mContext, R.string.baidu_service_error, Toast.LENGTH_SHORT).show();
            return;
        }
        FrontiaSdkControl.getInstance(mAuthorization).checkStatus(sinaBindView, SINAWEIBO);
        FrontiaSdkControl.getInstance(mAuthorization).checkStatus(tencentBindView, QQWEIBO);
        FrontiaSdkControl.getInstance(mAuthorization).checkStatus(renrenBindView, RENREN);
        FrontiaSdkControl.getInstance(mAuthorization).checkStatus(qqBindView, QQFRIEND);
        FrontiaSdkControl.getInstance(mAuthorization).checkStatus(qzoneBindView, QZONE);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.setting_account_binding_layout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sinaBindBtn:
                clickAction(v, SINAWEIBO);
                break;
            case R.id.tencentBindBtn:
                clickAction(v, QQWEIBO);
                break;
            case R.id.renrenBindBtn:
                clickAction(v, RENREN);
                break;
            case R.id.qqBindBtn:
                clickAction(v, QQFRIEND);
                break;
            case R.id.qqzoneBtn:
                clickAction(v, QZONE);
                break;
        }
    }

    private void clickAction(View v, FrontiaAuthorization.MediaType type) {
        if(mAuthorization == null) {
            Toast.makeText(mContext, R.string.baidu_service_error, Toast.LENGTH_SHORT).show();
            return;
        }
        if (FrontiaSdkControl.getInstance(mAuthorization).getStatus(type)) {
            FrontiaSdkControl.getInstance(mAuthorization).accountLogout(v, type);
        } else {
            FrontiaSdkControl.getInstance(mAuthorization).startAccountLogin(mContext, v, type);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mAuthorization) {
            mAuthorization.onActivityResult(requestCode, resultCode, data);
        }
    }
}
