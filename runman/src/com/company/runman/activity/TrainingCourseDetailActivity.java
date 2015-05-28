package com.company.runman.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.company.news.jsonform.TrainingCourseJsonform;
import com.company.news.query.TimeScheduleRelationSearchContion;
import com.company.news.validate.CommonsValidate;
import com.company.news.vo.TimeScheduleRelationVO;
import com.company.news.vo.TrainingCourseVO;
import com.company.runman.R;
import com.company.runman.activity.Adapter.TimesScheduleRelationWeekEditItemAdapter;
import com.company.runman.activity.Adapter.TimesScheduleRelationWeekShowItemAdapter;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.datacenter.model.BaseResultEntity;
import com.company.runman.net.HttpControl;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.net.request.DefaultRequest;
import com.company.runman.utils.*;
import com.company.runman.widget.DialogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by LMQ on 14-9-3.
 * Email:
 */
public class TrainingCourseDetailActivity extends BaseActivity {
    static private String ClassName="TrainingCourseDetailActivity";

    private TextView title;
    private TextView time_length;
    private TextView difficulty_degree;
    private TextView place;
    private TextView context;
    private TextView price;
//    private View save_btn;
    TextView textView_days;

    private Button button_add_time;
  //  private LinkedList mListItems;
    private TimesScheduleRelationWeekShowItemAdapter baseListAdapter;
    public static ListView listView;
    private TrainingCourseVO vo;
    private Calendar[] week1and7;

    @Override
    public void initView() {
        setHeaderTitle("课程详细");
    }


    public void updateTextView_days(){
        textView_days.setText(TimeUtils.getDateString(week1and7[0].getTime())+"~"+TimeUtils.getDateString(week1and7[1].getTime()));
        //记录星期一，方便生成订单真实时间
        baseListAdapter.setWeek1Cal(week1and7[0]);
    }
    @Override
    public void initData() {
        Intent intent = this.getIntent();
         vo=(TrainingCourseVO)intent.getSerializableExtra(Constant.ResponseData.DATA);
        if(vo==null){
            Toast.makeText(mContext, R.string.empty_data_text, Toast.LENGTH_SHORT).show();
            return;
        }
        baseListAdapter = new TimesScheduleRelationWeekShowItemAdapter(this,vo);
        listView.setAdapter(baseListAdapter);

        title.setText(Tool.objectToString(vo.getTitle()));
        time_length.setText(Tool.objectToString(vo.getTime_length()));
        price.setText(Tool.objectToString(vo.getPrice()));
        difficulty_degree.setText(Tool.objectToString(vo.getDifficulty_degree()));
       place.setText(Tool.objectToString(vo.getPlace()));
        context.setText(Tool.objectToString(vo.getContext()));

        week1and7=TimeUtils.convertWeek1To7ByDate(new Date());
        updateTextView_days();

        if(vo.getId()==null){
            return;
        }
       List time_list= vo.getTime_list();
        if(time_list!=null&&time_list.size()>0){
            baseListAdapter.addAll(time_list);
            Utility.setListViewHeightBasedOnChildren(listView);
        }


//        Utility.setListViewHeightBasedOnChildren(listView);
       //  new TimeScheduleRelationQueryAsyncTask(mContext, Constant.BusinessData.Time_schedule_relation_type_1.toString(), vo.getId()).execute();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.training_course_detail_layout);
//        exercise_mode1 = (RadioButton) findViewById(R.id.exercise_mode1);
//        exercise_mode2 = (RadioButton) findViewById(R.id.exercise_mode2);
        difficulty_degree = (TextView) findViewById(R.id.difficulty_degree);
        context = (TextView) findViewById(R.id.context);

//        CalendarView CalendarView=new CalendarView();
        place = (TextView) findViewById(R.id.place);
        title = (TextView) findViewById(R.id.title);
        time_length = (TextView) findViewById(R.id.time_length);
        price = (TextView) findViewById(R.id.price);

//        save_btn = findViewById(R.id.save_btn);
//        save_btn.setOnClickListener(this);


         textView_days = (TextView) findViewById(R.id.textView_days);
        findViewById(R.id.button_week_back).setOnClickListener(this);
        findViewById(R.id.button_week_next).setOnClickListener(this);
//        save_btn = findViewById(R.id.save_btn);
//        save_btn.setOnClickListener(this);
        //周期列表
        listView=(ListView)findViewById(R.id.timeScheduleRelationList);

    };

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.button_week_back:
                week1and7[0].add(Calendar.DAY_OF_MONTH, -7);
                week1and7[1].add(Calendar.DAY_OF_MONTH, -7);
                updateTextView_days();
                break;
            case R.id.button_week_next:
                week1and7[0].add(Calendar.DAY_OF_MONTH, 7);
                week1and7[1].add(Calendar.DAY_OF_MONTH, 7);
                updateTextView_days();
                break;
        }
    }


    public void finish() {
       // Intent intent = new Intent(mContext, MyTrainingCourseActivity.class);
        //  startActivity(intent);
        super.finish();
    }

}
