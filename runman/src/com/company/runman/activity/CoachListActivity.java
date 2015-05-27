package com.company.runman.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.company.news.jsonform.TrainingPlanJsonform;
import com.company.runman.R;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.datacenter.model.BaseResultEntity;
import com.company.runman.net.HttpControl;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.net.request.DefaultRequest;
import com.company.runman.utils.AbstractAsyncTask;
import com.company.runman.utils.Constant;
import com.company.runman.widget.DialogUtils;

/**
 * Created by EdisonZhao on 14-9-3.
 * Email:zhaoliangyu@sobey.com
 */
public class CoachListActivity extends BaseActivity {
    static private String ClassName="CoachListActivity";

    private TrainingPlanJsonform form;
    private RadioButton exercise_mode1;
    private RadioButton exercise_mode2;
    private EditText start_date;
    private EditText start_time;
    private EditText end_time;
    private EditText place;
    private EditText runKM;
    private EditText run_times;
    private EditText price;
    private View caach_course_detail_btn;



    @Override
    public void initView() {
        setHeaderTitle("教练列表");
        caach_course_detail_btn = findViewById(R.id.caach_course_detail_btn);
        caach_course_detail_btn.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.coach_list_layout);



    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == caach_course_detail_btn) {

        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent(mContext, MyInfoActivity.class);
        startActivity(intent);
        super.finish();
    }

}
