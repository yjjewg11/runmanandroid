package com.company.runman.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import com.company.runman.R;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.datacenter.model.UpgradeResponseEntity;
import com.company.runman.utils.Constant;
import com.company.runman.utils.LogHelper;
import com.company.runman.utils.TraceUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by EdisonZhao on 14-8-11.
 * Email:zhaoliangyu@sobey.com
 */
public class CheckVersionActivity extends BaseActivity {

    private ProgressDialog mProgressDialog;
    private static final String TAG = "CheckVersionActivity";
    private String fileURL = "";
    private File rootDir = Environment.getExternalStorageDirectory();
    private DownloadFileAsync downloadFileAsync;
    private UpgradeResponseEntity upgradeResponseEntity;

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        upgradeResponseEntity = (UpgradeResponseEntity) getIntent().getSerializableExtra(Constant.ExtraKey.EXTRA_COMMON_KEY);
        if (upgradeResponseEntity != null) {
            showUpdateDialog(upgradeResponseEntity);
        } else {
            finish();
        }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.check_version_layout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //如果取消了下载，关闭下载线程
        if (downloadFileAsync != null && AsyncTask.Status.FINISHED != downloadFileAsync.getStatus()) {
            downloadFileAsync.cancel(true);
            LogHelper.d(TAG, "Interrupt update Task Thread!");
        }
    }

    private void showUpdateDialog(final UpgradeResponseEntity upgradeResponse) {
        new AlertDialog.Builder(mContext).setTitle("最新版本为：V" + upgradeResponse.getVersion())
                .setMessage(upgradeResponse.getDescription())
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProgressDialog = new ProgressDialog(mContext);
                        mProgressDialog.setMessage("正在下载最新版本,请稍候...");
                        mProgressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_HORIZONTAL);
                        mProgressDialog.setProgressDrawable(getResources().getDrawable(R.drawable.progress_horizontal));
                        mProgressDialog.setIndeterminate(false);
                        mProgressDialog.setMax(100);
                        mProgressDialog.setCancelable(true);
                        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                finish();
                            }
                        });
                        mProgressDialog.show();
                        downloadFileAsync = new DownloadFileAsync();
                        downloadFileAsync.setActivity(mContext);
                        // 检查下载目录是否存在
                        downloadFileAsync.checkAndCreateDirectory(Constant.PRIVATE_DATA_PATH);
                        // 开始下载
                        downloadFileAsync.execute(upgradeResponse.getDownLink());
                    }
                })
                .setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                }).show();
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        private Context context;

        public void setActivity(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {
                // 连接地址
                URL u = new URL(aurl[0]);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                // 计算文件长度
                int lenghtOfFile = c.getContentLength();

                String[] fileNames = aurl[0].split("/");
                String fileName = fileNames[fileNames.length - 1];
                FileOutputStream f = new FileOutputStream(new File(rootDir + Constant.PRIVATE_DATA_PATH + "/", fileName));

                InputStream in = c.getInputStream();

                // 下载的代码
                byte[] buffer = new byte[1024];
                int len1 = 0;
                long total = 0;

                while ((len1 = in.read(buffer)) > 0) {
                    total += len1; // total = total + len1
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    f.write(buffer, 0, len1);
                }
                f.close();
                fileURL = rootDir + Constant.PRIVATE_DATA_PATH + "/" + fileName;
            } catch (Exception e) {
                LogHelper.d(TAG, e.getMessage());
                mProgressDialog.cancel();
                return null;
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            LogHelper.d(TAG, progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            mProgressDialog.cancel();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(fileURL)), "application/vnd.android.package-archive");
            context.startActivity(intent);
            finish();
        }

        public void checkAndCreateDirectory(String dirName) {
            try {
                File new_dir = new File(rootDir + dirName);
                if (!new_dir.exists()) {
                    new_dir.mkdirs();
                }
            } catch (Exception e) {
                TraceUtil.traceThrowableLog(e);
                LogHelper.e(TAG, e.getMessage());
            }

        }
    }
}
