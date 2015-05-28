package com.company.runman.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.company.news.jsonform.TrainingCourseJsonform;
import com.company.news.jsonform.UserRelationTrainingCourseJsonform;
import com.company.news.query.TimeScheduleRelationSearchContion;
import com.company.news.validate.CommonsValidate;
import com.company.news.vo.TimeScheduleRelationVO;
import com.company.news.vo.TrainingCourseVO;
import com.company.news.vo.UserRelationTrainingCourseVO;
import com.company.runman.R;
import com.company.runman.activity.Adapter.TimesScheduleRelationWeekEditItemAdapter;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.asynctask.AbstractDetailAsyncTask;
import com.company.runman.asynctask.AbstractSaveAsyncTask;
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
import java.util.Date;
import java.util.List;

/**
 * Created by LMQ on 14-9-3.
 * Email:
 */
public class TrainingCourseDetailSubscribeActivity extends BaseActivity {
    static private String ClassName="TrainingCourseDetailSubscribeActivity";

    private UserRelationTrainingCourseJsonform form;
    private TextView title;
    private TextView time_length;
    private TextView difficulty_degree;
    private TextView place;
    private TextView context;
    private TextView price;
    private TextView course_time;

    private View save_btn;

    private Button button_add_time;
  //  private LinkedList mListItems;
    private TimesScheduleRelationWeekEditItemAdapter baseListAdapter;
    private UserRelationTrainingCourseVO vo=null;
    Date subscribe_Date;
    TrainingCourseVO trainingCourseVO;
    TimeScheduleRelationVO timeScheduleRelationVO;
    @Override
    public void initView() {
        setHeaderTitle("课程详细");
    }

    @Override
    public void initData() {
         Intent intent = this.getIntent();
        Object dATA_ID=(Object)intent.getSerializableExtra(Constant.ResponseData.DATA_ID);
        if(dATA_ID!=null){
            String geturl= "rest/userRelationTrainingCourse/{uuid}.json";
            new AbstractDetailAsyncTask(dATA_ID,TrainingCourseDetailSubscribeActivity.this,geturl){
                @Override
                protected void onPostExecute2(JSONObject jsonObject) {
                    String dataStr = jsonObject.optString(Constant.ResponseData.DATA);
                     vo = (UserRelationTrainingCourseVO) new GsonUtils().stringToObject(dataStr, UserRelationTrainingCourseVO.class);
                }
            }.execute();
            return;
        }
         vo=(UserRelationTrainingCourseVO)intent.getSerializableExtra(Constant.ResponseData.DATA);
         subscribe_Date=(Date)intent.getSerializableExtra(Constant.ResponseData.Subscribe_Date);
         trainingCourseVO=(TrainingCourseVO)intent.getSerializableExtra(Constant.ResponseData.TrainingCourseVO);
         timeScheduleRelationVO=(TimeScheduleRelationVO)intent.getSerializableExtra(Constant.ResponseData.TimeScheduleRelationVO);


        reloadData();
    }
    /**
     * 重新加载数据：1。新建.2.修改订单
     */
    public void reloadData() {
        if(trainingCourseVO!=null){
            title.setText(Tool.objectToString(trainingCourseVO.getTitle()));
            time_length.setText(Tool.objectToString(trainingCourseVO.getTime_length()));
            price.setText(Tool.objectToString(trainingCourseVO.getPrice()));
            difficulty_degree.setText(Tool.objectToString(trainingCourseVO.getDifficulty_degree()));
            place.setText(Tool.objectToString(trainingCourseVO.getPlace()));
            context.setText(Tool.objectToString(trainingCourseVO.getContext()));
        }
        course_time.setText(TimeUtils.getTimeString(subscribe_Date));

        if(vo.getId()==null){
            return;
        }
    }
    @Override
    public void setContentView() {
        setContentView(R.layout.training_course_detail_subscribe_layout);
//        exercise_mode1 = (RadioButton) findViewById(R.id.exercise_mode1);
//        exercise_mode2 = (RadioButton) findViewById(R.id.exercise_mode2);
        difficulty_degree = (TextView) findViewById(R.id.difficulty_degree);
        context = (TextView) findViewById(R.id.context);

//        CalendarView CalendarView=new CalendarView();
        place = (TextView) findViewById(R.id.place);
        title = (TextView) findViewById(R.id.title);
        time_length = (TextView) findViewById(R.id.time_length);
        price = (TextView) findViewById(R.id.price);

        course_time = (TextView) findViewById(R.id.course_time);
        save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener(this);

//        button_add_time =(Button)findViewById(R.id.button_add_time);
//        button_add_time.setOnClickListener(this);
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == save_btn) {
            if(!Tool.isConnection(mContext)) {
                Toast.makeText(mContext, R.string.network_offline, Toast.LENGTH_SHORT).show();
                return;
            }
//            else if(TextUtils.isEmpty(place.getText())){
//                Toast.makeText(mContext, "地点不能为空!", Toast.LENGTH_SHORT).show();
//                return;
//            } else if(TextUtils.isEmpty(price.getText())) {
//                Toast.makeText(mContext, "价格不能为空!", Toast.LENGTH_SHORT).show();
//                return;
//            }else if(TextUtils.isEmpty(title.getText())) {
//                Toast.makeText(mContext, "标题不能为空!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//
//            if(!CommonsValidate.isDecimal(price.getText().toString())) {
//                Toast.makeText(mContext, "价格不是有效数据!", Toast.LENGTH_SHORT).show();
//                return;
//            }
            if(form==null){//创建
                form = new UserRelationTrainingCourseJsonform();

            }
//            if(exercise_mode2.isChecked()){
//                form.setExercise_mode(2);
//            }else{
//                form.setExercise_mode(1);
//            }
            try{
                form.setCourse_id(trainingCourseVO.getId());
                form.setCourse_time(TimeUtils.date2TimestampStart(subscribe_Date));
                form.setTime_schedule_id(timeScheduleRelationVO.getId());
            }catch (Exception e){
                TraceUtil.traceLog(TAG+"::"+e.getMessage());
            }

            showProgress(getString(R.string.progress_text));
            String saveurl="rest/userRelationTrainingCourse/save.json";
            new AbstractSaveAsyncTask(form,TrainingCourseDetailSubscribeActivity.this,saveurl){
                @Override
                protected void onPostExecute2(JSONObject jsonObject) {

                    Intent intent = new Intent(mContext, MyInfoActivity.class);
                      startActivity(intent);
//                    String data_id = jsonObject.optString(Constant.ResponseData.DATA_ID);
//                    form.setId(Long.valueOf(data_id));

                }
            }.execute();
        }
    }

    @Override
    public void finish() {
//        Intent intent = new Intent(mContext, TrainingCourseListPublishActivity.class);
//        startActivity(intent);
        super.finish();
    }

}
