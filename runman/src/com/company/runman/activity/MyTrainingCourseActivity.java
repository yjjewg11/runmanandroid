package com.company.runman.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.company.news.query.NSearchContion;
import com.company.news.vo.TrainingCourseVO;
import com.company.runman.R;
import com.company.runman.activity.Adapter.MyTrainingCourseAdapter;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.asynctask.AbstractDetailTrainingCourseAsyncTask;
import com.company.runman.datacenter.model.BaseResultEntity;
import com.company.runman.net.HttpControl;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.net.request.DefaultRequest;
import com.company.runman.utils.*;
import com.company.runman.widget.DialogUtils;
import com.company.runman.widget.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/20.
 */
public class MyTrainingCourseActivity extends BaseActivity {
    static public String TAG="MyTrainingCourseActivity";

    private LinkedList<String> mListItems;
  //  private ListView listView;
    private MyTrainingCourseAdapter baseListAdapter;
    private Button button_add_training_course;
    public static PullToRefreshListView listView;

    private Button button_more;

    View view;
    PopupWindow pop;

    @Override
    protected void onStart() {
        super.onStart();
        new TrainingCourseListMyAsyncTask(this).execute();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.my_training_course);
        button_add_training_course=(Button) findViewById(R.id.button_add_training_course);
        button_add_training_course.setOnClickListener(this);
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView = (PullToRefreshListView) findViewById(R.id.listViewMyTrainingCourse);
        // Set a listener to be invoked when the list should be refreshed.
        listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                new TrainingCourseListMyAsyncTask(MyTrainingCourseActivity.this, Constant.Query.Operate_Refresh).execute();
            }
            @Override
            public void onLoadMore() {
                new TrainingCourseListMyAsyncTask(MyTrainingCourseActivity.this, Constant.Query.Operate_LoadMore).execute();
            }
        });

        //运动计划list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                // TODO Auto-generated method stub
                try {
                    Object o = arg0.getItemAtPosition(arg2);
                    if (o == null) {
                        DialogUtils.alertErrMsg(mContext, arg2 + " index,mListItems.get() is null");
                    }
                    if (o instanceof TrainingCourseVO) {
                        TrainingCourseVO tmp = (TrainingCourseVO) o;
                         new AbstractDetailTrainingCourseAsyncTask(mContext, tmp.getId().toString()) {
                             public    void onPostExecute2(TrainingCourseVO vo){
                                 IntentUtils.startTrainingCourseEditActivity(mContext, vo);
                             }
                        }.execute();
                        // showProgress("加载数据");
                       // new EditTrainingCourseAsyncTask(tmp.getId().toString()).execute();

                    }{

                    }

                   // Toast.makeText(mContext, "" + arg2, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    TraceUtil.traceThrowableLog(e);
                    DialogUtils.alertErrMsg(mContext, arg2 + " index," + e.getMessage());
                }


            }


        });

        mListItems = new LinkedList<String>();
       // mListItems.addAll(Arrays.asList(mStrings));

        baseListAdapter = new MyTrainingCourseAdapter(this);
        listView.setAdapter(baseListAdapter);


        new TrainingCourseListMyAsyncTask(MyTrainingCourseActivity.this, 0).execute();
    }


    @Override
    protected void onResume() {
//        Intent intent = this.getIntent();
//        String userinfoStr =(String)intent.getSerializableExtra("userinfo_update");
//        intent.removeExtra("userinfo_update");
//        if(userinfoStr!=null){
//            loadUserifoToshow();
//        }

      //  loadUserifoToshow();
        super.onResume();

    }

    @Override
    public void initData() {
//        HashMap<String, List<TrainingCourseVO>> mEventMap
//                = (HashMap<String, List<TrainingCourseVO>>) getIntent().getSerializableExtra(Constant.ExtraKey.EXTRA_COMMON_ARRAY_KEY);
//        String typeId = getIntent().getStringExtra(Constant.ExtraKey.EXTRA_COMMON_KEY);


    }

    @Override
    public void initView() {

        button_add_training_course=(Button) findViewById(R.id.button_add_training_course);
        button_add_training_course.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_training_course:
                IntentUtils.startTrainingCourseEditActivity(mContext, null);
                break;

        }


    }


    /**
     * 查询训练计划
     */
    private class TrainingCourseListMyAsyncTask extends AbstractAsyncTask<String, Void, Object> {

        private Context context;
        private int operate;
        private NSearchContion nSearchContion;

        public TrainingCourseListMyAsyncTask(Context context, int operate) {
            this.context = context;
            this.operate = operate;
        }
        public TrainingCourseListMyAsyncTask(Context context) {
            this.context = context;
            this.operate = 0;
        }
        @Override
        protected Object doInBackground(String[] params) {
            NSearchContion nSearchContion=baseListAdapter.getsSearchContion();
            if (operate ==Constant.Query.Operate_LoadMore ) {
                nSearchContion.addPageNo();
            }else{
               // nSearchContion.getPsoData().setPageSize(2);
                nSearchContion.restPageNo();
            }
          //  nSearchContion .getPsoData().setPageNo(1);
            String url = "rest/trainingCourse/my.json";
            DefaultRequest request = new DefaultRequest(mContext, url, Constant.RequestCode.REQUEST_GET);
            request.setParaObj(nSearchContion);
            HttpReturn httpReturn = HttpControl.execute(request, mContext);
            IResponse response = new IResponse();
            return response.parseFrom(httpReturn);
        }

        @Override
        protected void onPostExecute(Object result) {
            //恢复按钮文字为登录

            if (operate ==Constant.Query.Operate_Refresh ) {
                // 将字符串“Added after refresh”添加到顶部
                mListItems.addFirst("下拉刷新");

                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy-MM-dd-  HH:mm:ss");
                String date = format.format(new Date());
                // Call onRefreshComplete when the list has been refreshed.
                listView.onRefreshComplete(date);
            } else if (operate ==Constant.Query.Operate_LoadMore ) {
                mListItems.addLast("显示更多");
                listView.onLoadMoreComplete();
            }


            try {
                super.onPostExecute(result);
                BaseResultEntity resultEntity = (BaseResultEntity) result;
                if (!Constant.ResponseData.STATUS_SUCCESS.equals(resultEntity.getStatus())) {
                    DialogUtils.alertErrMsg(mContext, resultEntity.getResultMsg());
                    return;
                }
                JSONObject jsonObject = (JSONObject) resultEntity.getResultObject();
                String   dataArrStr = jsonObject.optString(Constant.ResponseData.DATA);
                Gson gson = new GsonUtils().getGson();
                List<TrainingCourseVO> list = gson.fromJson(dataArrStr, new TypeToken<List<TrainingCourseVO>>() {
                }.getType());

                if (operate ==Constant.Query.Operate_LoadMore ) {
                    baseListAdapter.addAll(list);
                }else{
                    baseListAdapter.setList(list);
                }
                if(list.size()==0){
                    Toast.makeText(mContext, R.string.empty_data_text, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext, "加载"+list.size()+"条", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.e(TAG, "onPostExecute:", e);
                TraceUtil.traceThrowableLog(e);
                DialogUtils.alertErrMsg(mContext,TAG+ "onPostExecute:" + e.getMessage());
            }
        }
    }
}