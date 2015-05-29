package com.company.runman.activity;

import android.content.Context;
import android.content.Intent;
import com.company.news.query.NSearchContion;
import com.company.news.vo.TrainingCourseVO;
import com.company.news.vo.TrainingPlanVO;
import com.company.runman.R;
import com.company.runman.activity.Adapter.DefaultAdapter;
import com.company.runman.activity.Adapter.TrainingCoursePublishAdapter;
import com.company.runman.asynctask.AbstractDetailTrainingCourseAsyncTask;
import com.company.runman.asynctask.AbstractPullToRefreshListAsyncTask;
import com.company.runman.utils.Constant;
import com.company.runman.utils.GsonUtils;
import com.company.runman.utils.IntentUtils;
import com.company.runman.widget.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Administrator on 2015/5/28.
 */
public class TrainingCourseListActivity extends AbstractPullToRefreshListActivity {

    @Override
    protected void asyncTaskExecute(Integer operate) {
        new TrainingCourseListPublishAsyncTask(mContext,operate,listView).execute();
    }


    DefaultAdapter baseListAdapter;
    public  DefaultAdapter getBaseListAdapter(){

        if(baseListAdapter==null){
            baseListAdapter= new TrainingCoursePublishAdapter(this);
            Intent intent = this.getIntent();
            Object userid=intent.getSerializableExtra(Constant.ResponseData.DATA);
            if(userid!=null){
                NSearchContion nSearchContion = baseListAdapter.getsSearchContion();
                nSearchContion.setUserid(userid.toString());
            }
        }


        return baseListAdapter;
    }

    public  void listViewOnItemClick(Object o){
        if (o instanceof TrainingCourseVO) {

            new AbstractDetailTrainingCourseAsyncTask(mContext, ((TrainingCourseVO)o).getId().toString()) {
                public    void onPostExecute2(TrainingCourseVO vo){
                    IntentUtils.startTrainingCourseDetailActivity(mContext, vo);
                }
            }.execute();
        }

    }

    public  Integer getCurrActivityLayout(){
        return R.layout.training_course_list_publish;
    }

    public Integer getCurrActivityListView(){
        return  R.id.listViewTrainingCourse;
    }

    public class TrainingCourseListPublishAsyncTask extends AbstractPullToRefreshListAsyncTask {

        public TrainingCourseListPublishAsyncTask(Context context, Integer operate, PullToRefreshListView listView) {
            super(context, operate, listView);
        }
        public List jsonToList(String dataArrStr){
            Gson gson = new GsonUtils().getGson();
            List<TrainingCourseVO> list = gson.fromJson(dataArrStr, new TypeToken<List<TrainingCourseVO>>() {
            }.getType());
            return list;
        }
        @Override
        public String getQueryUrl() {
            return "rest/trainingCourse/queryPublish.json";
        }
    }
}
