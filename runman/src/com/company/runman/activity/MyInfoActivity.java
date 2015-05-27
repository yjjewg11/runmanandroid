package com.company.runman.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.company.news.query.NSearchContion;
import com.company.news.vo.TrainingPlanVO;
import com.company.news.vo.UserInfoReturn;
import com.company.runman.R;
import com.company.runman.activity.Adapter.TrainingPlanAdapter;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.cache.ImgDownCache;
import com.company.runman.datacenter.model.BaseResultEntity;
import com.company.runman.datacenter.provider.SharePreferenceProvider;
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
import java.util.*;

/**
 * Created by Administrator on 2015/5/20.
 */
public class MyInfoActivity extends BaseActivity {
    static public String TAG="MyInfoActivity";
    private ImageView imageView_myhead;
    private TextView name;
    private TextView myinfo_detail;
    private TextView modify_myinfo;

    private TextView add_training_plan;
    private LinkedList<String> mListItems;
  //  private ListView listView;
    private TrainingPlanAdapter baseListAdapter;

    public static PullToRefreshListView listView;

    private Button button_more;

    View view;
    PopupWindow pop;

    @Override
    protected void onStart() {
        super.onStart();
        new trainingPlanListMyAsyncTask(this).execute();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.myinfo_layout);
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView = (PullToRefreshListView) findViewById(R.id.listViewMytrainingPlan);
        // Set a listener to be invoked when the list should be refreshed.
        listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                new trainingPlanListMyAsyncTask(MyInfoActivity.this, Constant.Query.Operate_Refresh).execute();
            }
            @Override
            public void onLoadMore() {
                new trainingPlanListMyAsyncTask(MyInfoActivity.this, Constant.Query.Operate_LoadMore).execute();
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
                    if (o instanceof TrainingPlanVO) {
                        TrainingPlanVO tmp = (TrainingPlanVO) o;
                        //加载详细页面
//
//                        Intent intent = new Intent(mContext, TrainingPlanDetailActivity.class);
//                        startActivity(intent);

                        showProgress("加载数据");
                        new DetailTrainingPlanAsyncTask(tmp.getId().toString()).execute();

                    }

                    Toast.makeText(mContext, "" + arg2, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    TraceUtil.traceThrowableLog(e);
                    DialogUtils.alertErrMsg(mContext, arg2 + " index," + e.getMessage());
                }


            }


        });

        mListItems = new LinkedList<String>();
       // mListItems.addAll(Arrays.asList(mStrings));

        baseListAdapter = new TrainingPlanAdapter(this);
        listView.setAdapter(baseListAdapter);
        loadUserifoToshow();

        new trainingPlanListMyAsyncTask(MyInfoActivity.this, 0).execute();
    }

    /**
     * 加载用户信息
     */
    private  void loadUserifoToshow(){
        String userinfoStr = SharePreferenceProvider.getInstance(mContext).getUserinfo();
        UserInfoReturn user=(UserInfoReturn)new GsonUtils().stringToObject(userinfoStr, UserInfoReturn.class);
        try {
            name.setText(user.getName());
            myinfo_detail.setText(user.getSex()==1?"女":"男"+" "+user.getCity());
            if(!TextUtils.isEmpty(user.getHead_imgurl())) {
                ImgDownCache.getInstance(mContext).displayImage(Tool.getFullUrl(user.getHead_imgurl()), imageView_myhead);
            }
        } catch (Exception e) {
            TraceUtil.traceThrowableLog(e);
            TraceUtil.traceLog("userinfoStr="+userinfoStr);
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
//        Intent intent = this.getIntent();
//        String userinfoStr =(String)intent.getSerializableExtra("userinfo_update");
//        intent.removeExtra("userinfo_update");
//        if(userinfoStr!=null){
//            loadUserifoToshow();
//        }

        loadUserifoToshow();
        super.onResume();

    }

    @Override
    public void initData() {
//        HashMap<String, List<TrainingPlanVO>> mEventMap
//                = (HashMap<String, List<TrainingPlanVO>>) getIntent().getSerializableExtra(Constant.ExtraKey.EXTRA_COMMON_ARRAY_KEY);
//        String typeId = getIntent().getStringExtra(Constant.ExtraKey.EXTRA_COMMON_KEY);


    }

    @Override
    public void initView() {
        imageView_myhead = (ImageView) findViewById(R.id.imageView_myhead);
        name = (TextView) findViewById(R.id.name);
        myinfo_detail = (TextView) findViewById(R.id.myinfo_detail);
        modify_myinfo = (TextView) findViewById(R.id.modify_myinfo);
        modify_myinfo.setOnClickListener(this);

        add_training_plan=(TextView) findViewById(R.id.add_training_plan);
        add_training_plan.setOnClickListener(this);

        button_more=(Button) findViewById(R.id.button_more);
        button_more.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.modify_myinfo:
                Intent intent = new Intent(mContext, MyinfoModifyActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.add_training_plan:
                IntentUtils.startTrainingPlanEditActivity(mContext, null);
                break;
            case R.id.button_more:
                showPopupWindow(view);
                break;
        }


    }


    public void showPopupWindow(View v){
        if(view==null)
        view = this.getLayoutInflater().inflate(R.layout.popup_user_menu, null);
        if(pop==null) pop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setTouchable(true);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));

        ((Button) view.findViewById(R.id.button_Coach_mode)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CoachModifyActivity.class);
                startActivity(intent);
                pop.dismiss();
                // TODO Auto-generated method stub

            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        pop.showAsDropDown(v);
    }
    /**
     * 编辑训练计划
     */
    private class DetailTrainingPlanAsyncTask extends AbstractAsyncTask<String, Void, Object> {
        private String id;

        public DetailTrainingPlanAsyncTask(String id) {
            this.id = id;
        }

        @Override
        protected Object doInBackground(String[] params) {

            String url = "rest/trainingPlan/{uuid}.json";
            url = url.replace("{uuid}", id);
            DefaultRequest request = new DefaultRequest(mContext, url, Constant.RequestCode.REQUEST_GET);

            HttpReturn httpReturn = HttpControl.execute(request, mContext);

            IResponse response = new IResponse();
            return response.parseFrom(httpReturn);
        }

        @Override
        protected void onPostExecute(Object result) {
            dismissProgress();
            try {
                super.onPostExecute(result);
                BaseResultEntity resultEntity = (BaseResultEntity) result;
                if (!Constant.ResponseData.STATUS_SUCCESS.equals(resultEntity.getStatus())) {
                    DialogUtils.alertErrMsg(mContext, resultEntity.getResultMsg());
                    return;
                }
                JSONObject jsonObject = (JSONObject) resultEntity.getResultObject();
                String dataArrStr = jsonObject.optString(Constant.ResponseData.DATA);
                TrainingPlanVO vo = (TrainingPlanVO) new GsonUtils().stringToObject(dataArrStr, TrainingPlanVO.class);
//                IntentUtils.startTrainingPlanEditActivity(mContext, vo);
                IntentUtils.startTrainingPlanDetailActivity(mContext, vo);
            } catch (Exception e) {
                TraceUtil.traceThrowableLog(e);
                DialogUtils.alertErrMsg(mContext,TAG+ "onPostExecute:" + e.getMessage());
            }

        }
    }

    /**
     * 查询训练计划
     */
    private class trainingPlanListMyAsyncTask extends AbstractAsyncTask<String, Void, Object> {

        private Context context;
        private int operate;
        private NSearchContion nSearchContion;

        public trainingPlanListMyAsyncTask(Context context, int operate) {
            this.context = context;
            this.operate = operate;
        }
        public trainingPlanListMyAsyncTask(Context context) {
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
            String url = "rest/trainingPlan/my.json";
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
                List<TrainingPlanVO> list = gson.fromJson(dataArrStr, new TypeToken<List<TrainingPlanVO>>() {
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
                TraceUtil.traceThrowableLog(e);
                DialogUtils.alertErrMsg(mContext,TAG+ "onPostExecute:" + e.getMessage());
            }
        }
    }
}