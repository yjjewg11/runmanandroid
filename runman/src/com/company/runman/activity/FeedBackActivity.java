package com.company.runman.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.company.runman.R;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.datacenter.provider.CategoryDataProvider;
import com.company.runman.utils.Constant;
import com.company.runman.utils.AbstractAsyncTask;

/**
 * Created by EdisonZhao on 14-10-24.
 * Email:zhaoliangyu@sobey.com
 */
public class FeedBackActivity extends BaseActivity {

    private Dialog dialog;
    private EditText mContent;
    private EditText mPhone;
    private Button mCommit;

    @Override
    public void initView() {
        mContent = (EditText) findViewById(R.id.feedback_content);
        mPhone = (EditText) findViewById(R.id.feedback_phone);
        mCommit = (Button) findViewById(R.id.feedback_commit);
        mCommit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        setHeaderTitle(R.string.feedback_title);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.feedback_layout);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.feedback_commit:
                if (mPhone.length() <= 0) {
                    Toast.makeText(mContext, R.string.feedback_error_phone, Toast.LENGTH_SHORT).show();
                    return;
                } else if(mContent.length() <= 0) {
                    Toast.makeText(mContext, R.string.feedback_error_content, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    dialog = new ProgressDialog(mContext);
                    dialog.setCancelable(true);
                    dialog.show();
                    dialog.setContentView(R.layout.progress_layout);
                    new FeedBackAsyncTask().execute("");
                }
                break;
        }
    }

    class FeedBackAsyncTask extends AbstractAsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("content", mContent.getText().toString());
            contentValues.put("tel", mPhone.getText().toString());
            return CategoryDataProvider.getInstance().feedBack(mContext, contentValues);
        }

        @Override
        protected void onPostExecute(Object object) {
            super.onPostExecute(object);
            dialog.dismiss();
            ContentValues contentValues = (ContentValues) object;
            String msg = contentValues.getAsString(Constant.ResponseData.MESSAGE);
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            if (contentValues.getAsBoolean(Constant.ResponseData.STATUS)) {
                finish();
            }
        }
    }
}
