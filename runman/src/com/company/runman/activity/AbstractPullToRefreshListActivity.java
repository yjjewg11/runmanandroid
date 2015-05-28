package com.company.runman.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import com.company.news.vo.TrainingCourseVO;
import com.company.runman.R;
import com.company.runman.activity.Adapter.DefaultAdapter;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.asynctask.AbstractPullToRefreshListAsyncTask;
import com.company.runman.utils.Constant;
import com.company.runman.utils.IntentUtils;
import com.company.runman.utils.TraceUtil;
import com.company.runman.widget.DialogUtils;
import com.company.runman.widget.PullToRefreshListView;

/**
 * Created by Administrator on 2015/5/20.
 */
public abstract class AbstractPullToRefreshListActivity extends BaseActivity {
    static public String TAG="AbstractPullToRefreshListActivity";
    /**
     * 列表item适配器
     * @return
     */
    public abstract DefaultAdapter getBaseListAdapter() ;

    public static PullToRefreshListView listView;

    View view;


    /**
     * 执行异常查询任务
     * @param operate
     */
    protected abstract void asyncTaskExecute(Integer operate);


    @Override
    protected void onStart() {
        super.onStart();
//        new TrainingCourseListMyAsyncTask(this).execute();
        asyncTaskExecute( Constant.Query.Operate_Refresh);

    }

    /**
     * R.layout.my_training_course
     * @return
     */
    public abstract Integer getCurrActivityLayout();

    @Override
    public void setContentView() {
        setContentView(getCurrActivityLayout());
    }

    /**
     * 列表layout
     * R.id.listViewTrainingCourse
     * @return
     */
    public abstract Integer getCurrActivityListView();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  new TrainingCourseListMyAsyncTask(AbstractTrainingCourseListActivity.this, 0).execute();
    }

    /**
     * item 点击触发事件
     * @param vo
     */
    public abstract void listViewOnItemClick(TrainingCourseVO vo);


    @Override
    public void initView() {


        listView = (PullToRefreshListView) findViewById(getCurrActivityListView());
        // Set a listener to be invoked when the list should be refreshed.
        listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                asyncTaskExecute( Constant.Query.Operate_Refresh);
                //  new TrainingCourseListMyAsyncTask(AbstractTrainingCourseListActivity.this, Constant.Query.Operate_Refresh).execute();
            }
            @Override
            public void onLoadMore() {
                asyncTaskExecute(Constant.Query.Operate_LoadMore);
//                new TrainingCourseListMyAsyncTask(AbstractTrainingCourseListActivity.this, Constant.Query.Operate_LoadMore).execute();
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

                        listViewOnItemClick( (TrainingCourseVO) o);

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


        listView.setAdapter(getBaseListAdapter());

        asyncTaskExecute( Constant.Query.Operate_Refresh);
    }


}