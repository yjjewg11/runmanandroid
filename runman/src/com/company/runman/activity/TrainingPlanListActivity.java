package com.company.runman.activity;

import android.content.Context;
import android.content.Intent;
import com.company.news.query.NSearchContion;
import com.company.news.vo.TrainingPlanVO;
import com.company.runman.R;
import com.company.runman.activity.Adapter.DefaultAdapter;
import com.company.runman.activity.Adapter.TrainingPlanAdapter;
import com.company.runman.asynctask.AbstractDetailAsyncTask;
import com.company.runman.asynctask.AbstractPullToRefreshListAsyncTask;
import com.company.runman.utils.Constant;
import com.company.runman.utils.GsonUtils;
import com.company.runman.utils.IntentUtils;
import com.company.runman.widget.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/5/28.
 */
public class TrainingPlanListActivity extends AbstractPullToRefreshListActivity {
    //"rest/trainingPlan/queryPublish.json"
    private String url="";
    @Override
    protected void asyncTaskExecute(Integer operate) {
        new TrainingPlanListPublishAsyncTask(mContext,operate,listView,url).execute();
    }

    @Override
    public void initData() {
        super.initData();
    }

    DefaultAdapter baseListAdapter;
    public  DefaultAdapter getBaseListAdapter(){
        Intent intent = this.getIntent();
        url=(String)intent.getSerializableExtra("url");
        if(baseListAdapter==null){
            baseListAdapter= new TrainingPlanAdapter(this);
            Object userid=intent.getSerializableExtra(Constant.ResponseData.DATA);
            if(userid!=null){
                NSearchContion nSearchContion = baseListAdapter.getsSearchContion();
                nSearchContion.setUserid(userid.toString());
            }
        }


        return baseListAdapter;
    }

    public  void listViewOnItemClick(Object o){

        if (o instanceof TrainingPlanVO) {
            String geturl= "rest/trainingPlan/{uuid}.json";
            TrainingPlanVO vo=(TrainingPlanVO)o;
            new AbstractDetailAsyncTask(vo.getId(),TrainingPlanListActivity.this,geturl) {
                public    void onPostExecute2(JSONObject jsonObject){
                    String jsonObjectStr = jsonObject.optString(Constant.ResponseData.DATA);
                    TrainingPlanVO vo = (TrainingPlanVO) new GsonUtils().stringToObject(jsonObjectStr, TrainingPlanVO.class);
                    IntentUtils.startTrainingPlanDetailActivity(mContext, vo);
                }
            }.execute();
        }

    }

    public  Integer getCurrActivityLayout(){
        return R.layout.training_plan_list_layout;
    }

    public Integer getCurrActivityListView(){
        return  R.id.listView;
    }

    public class TrainingPlanListPublishAsyncTask extends AbstractPullToRefreshListAsyncTask {
        String url;
        public TrainingPlanListPublishAsyncTask(Context context, Integer operate, PullToRefreshListView listView,String url) {
            super(context, operate, listView);
            this.url=url;
        }
        public List jsonToList(String dataArrStr){
            Gson gson = new GsonUtils().getGson();
            List<TrainingPlanVO> list = gson.fromJson(dataArrStr, new TypeToken<List<TrainingPlanVO>>() {
            }.getType());
            return list;
        }
        @Override
        public String getQueryUrl() {
            return url;
        }
    }
}
