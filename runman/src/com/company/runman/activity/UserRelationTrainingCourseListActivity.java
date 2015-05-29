package com.company.runman.activity;

import android.content.Context;
import android.content.Intent;
import com.company.news.vo.UserRelationTrainingCourseVO;
import com.company.runman.R;
import com.company.runman.activity.Adapter.DefaultAdapter;
import com.company.runman.activity.Adapter.UserRelationTrainingCourseListAdapter;
import com.company.runman.asynctask.AbstractPullToRefreshListAsyncTask;
import com.company.runman.utils.GsonUtils;
import com.company.runman.utils.IntentUtils;
import com.company.runman.widget.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Administrator on 2015/5/28.
 */
public class UserRelationTrainingCourseListActivity extends AbstractPullToRefreshListActivity {
    //[add]查询我预订的课程（query/subscribe/my）
    //[add]根据课程查询预订数据（query/byCourse）
    //[add]查询我的课程销售数据（query/myCourse/sales）
    private String url="";
    @Override
    protected void asyncTaskExecute(Integer operate) {
        new TrainingCourseListPublishAsyncTask(mContext,operate,listView,url).execute();
    }


    DefaultAdapter baseListAdapter;
    public  DefaultAdapter getBaseListAdapter(){
        Intent intent = this.getIntent();
        url=(String)intent.getSerializableExtra("url");
        if(baseListAdapter==null){
            baseListAdapter= new UserRelationTrainingCourseListAdapter(this);
//            Intent intent = this.getIntent();
//            Object userid=intent.getSerializableExtra(Constant.ResponseData.DATA);
//            if(userid!=null){
//                NSearchContion nSearchContion = baseListAdapter.getsSearchContion();
//                nSearchContion.setUserid(userid.toString());
//            }
        }


        return baseListAdapter;
    }

    public  void listViewOnItemClick(Object o){

        if(o instanceof UserRelationTrainingCourseVO){
            UserRelationTrainingCourseVO vo=(UserRelationTrainingCourseVO)o;
            IntentUtils.startTrainingCourseDetailSubscribeActivity(mContext, vo);
        }


    }

    public  Integer getCurrActivityLayout(){
        return R.layout.user_relation_training_course_list_layout;
    }

    public Integer getCurrActivityListView(){
        return  R.id.listView;
    }

    public class TrainingCourseListPublishAsyncTask extends AbstractPullToRefreshListAsyncTask {
        String url;
        public TrainingCourseListPublishAsyncTask(Context context, Integer operate, PullToRefreshListView listView,String url) {
            super(context, operate, listView);
            this.url=url;
        }
        public List jsonToList(String dataArrStr){
            Gson gson = new GsonUtils().getGson();
            List<UserRelationTrainingCourseVO> list = gson.fromJson(dataArrStr, new TypeToken<List<UserRelationTrainingCourseVO>>() {
            }.getType());
            return list;
        }
        @Override
        public String getQueryUrl() {
            return this.url;
        }
    }
}
