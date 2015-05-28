package com.company.runman.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import com.company.news.jsonform.TrainingPlanJsonform;
import com.company.news.vo.TrainingCourseVO;
import com.company.news.vo.UserVO;
import com.company.runman.R;
import com.company.runman.activity.Adapter.CoachListAdapter;
import com.company.runman.activity.Adapter.DefaultAdapter;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.asynctask.AbstractPullToRefreshListAsyncTask;
import com.company.runman.utils.GsonUtils;
import com.company.runman.widget.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by LMQ on 14-9-3.
 * Email:
 */
public class CoachListActivity extends AbstractPullToRefreshListActivity {
    static private String ClassName="CoachListActivity";
    @Override
    public void initView() {
        super.initView();
        setHeaderTitle("教练列表");
    }

    @Override
    public void initData() {


    }

    @Override
    public DefaultAdapter getBaseListAdapter() {
        return new CoachListAdapter(this);
    }


    @Override
    protected void asyncTaskExecute(Integer operate) {
        new CoachListAsyncTask(mContext,operate,listView).execute();
    }


    @Override
    public Integer getCurrActivityLayout() {
        return R.layout.coach_list_layout;
    }


    @Override
    public Integer getCurrActivityListView() {
        return R.id.listViewCoachList;
    }

    @Override
    public void listViewOnItemClick(TrainingCourseVO vo) {

    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void finish() {
//        Intent intent = new Intent(mContext, MyInfoActivity.class);
//        startActivity(intent);
        super.finish();
    }
    public class CoachListAsyncTask extends AbstractPullToRefreshListAsyncTask {

        public CoachListAsyncTask(Context context, Integer operate,PullToRefreshListView listView) {
            super(context, operate, listView);
        }
        public List jsonToList(String dataArrStr){
            Gson gson = new GsonUtils().getGson();
            List<UserVO> list = gson.fromJson(dataArrStr, new TypeToken<List<UserVO>>() {
            }.getType());
            return list;
        }
        @Override
        public String getQueryUrl() {
            return "rest/coach/query.json";
        }
    }
}
