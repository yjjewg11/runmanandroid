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
import java.util.List;

/**
 * Created by LMQ on 14-9-3.
 * Email:
 */
public class TrainingCourseDetailActivity extends BaseActivity {
    static private String ClassName="TrainingCourseDetailActivity";

    private TrainingCourseJsonform form;
    private TextView title;
    private TextView time_length;
    private TextView difficulty_degree;
    private TextView place;
    private TextView context;
    private TextView price;
    private View save_btn;

    private Button button_add_time;
  //  private LinkedList mListItems;
    private TimesScheduleRelationWeekEditItemAdapter baseListAdapter;
    public static ListView listView;
    private TrainingCourseVO vo=null;


    @Override
    public void initView() {
        setHeaderTitle("课程详细");
    }

    @Override
    public void initData() {
        Intent intent = this.getIntent();
         vo=(TrainingCourseVO)intent.getSerializableExtra(Constant.ResponseData.DATA);
        if(vo==null){
            vo=new TrainingCourseVO();
          
            vo.setExercise_mode(1);
        }else{
            form=new TrainingCourseJsonform();
            form.setId(vo.getId());
        }

        title.setText(Tool.objectToString(vo.getTitle()));
        time_length.setText(Tool.objectToString(vo.getTime_length()));
        price.setText(Tool.objectToString(vo.getPrice()));
        difficulty_degree.setText(Tool.objectToString(vo.getDifficulty_degree()));
       place.setText(Tool.objectToString(vo.getPlace()));
        context.setText(Tool.objectToString(vo.getContext()));

        if(vo.getId()==null){
            return;
        }
       List time_list= vo.getTime_list();
        if(time_list!=null&&time_list.size()>0){
            baseListAdapter.addAll(time_list);
        }


        Utility.setListViewHeightBasedOnChildren(listView);
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

        save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener(this);
        //周期列表
        listView=(ListView)findViewById(R.id.timeScheduleRelationList);



        baseListAdapter = new TimesScheduleRelationWeekEditItemAdapter(this);
        listView.setAdapter(baseListAdapter);
        Utility.setListViewHeightBasedOnChildren(listView);

    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == save_btn) {
            if(!Tool.isConnection(mContext)) {
                Toast.makeText(mContext, R.string.network_offline, Toast.LENGTH_SHORT).show();
                return;
            } else if(TextUtils.isEmpty(place.getText())){
                Toast.makeText(mContext, "地点不能为空!", Toast.LENGTH_SHORT).show();
                return;
            } else if(TextUtils.isEmpty(price.getText())) {
                Toast.makeText(mContext, "价格不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }else if(TextUtils.isEmpty(title.getText())) {
                Toast.makeText(mContext, "标题不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }
       

            if(!CommonsValidate.isDecimal(price.getText().toString())) {
                Toast.makeText(mContext, "价格不是有效数据!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(form==null){//创建
                form = new TrainingCourseJsonform();
            }
//            if(exercise_mode2.isChecked()){
//                form.setExercise_mode(2);
//            }else{
//                form.setExercise_mode(1);
//            }
            try{

                form.setTitle(title.getText().toString());
                form.setTime_length(Tool.convertInteger(time_length.getText().toString()));
                form.setDifficulty_degree(Tool.convertInteger(difficulty_degree.getText().toString()));
                form.setContext(context.getText().toString());
            form.setPlace(place.getText().toString());
            if(!TextUtils.isEmpty(price.getText())){
                form.setPrice(Tool.convertDouble(price.getText().toString()));
            }

            }catch (Exception e){
                TraceUtil.traceLog(TAG+"::"+e.getMessage());
            }

            showProgress(getString(R.string.progress_text));

            if(baseListAdapter.getList().size()==0){
                Toast.makeText(mContext,"没有设置时间！", Toast.LENGTH_SHORT).show();
                return;
            }
            updateViewsToList();
            form.setTime_list(baseListAdapter.getList());
            new SaveAsyncTask(form,this).execute("");
        }else if(v==button_add_time){
            baseListAdapter.add(new TimeScheduleRelationVO());
            Utility.setListViewHeightBasedOnChildren(listView);
        }
    }


    public boolean updateViewsToList() {
        try{
        for(int i=0;i<this.baseListAdapter.getList().size();i++){
            TimeScheduleRelationVO tvo=(TimeScheduleRelationVO)this.baseListAdapter.getList().get(i);
            tvo.setType(Constant.BusinessData.Time_schedule_relation_type_1);
            tvo.setTime_period(Constant.BusinessData.Time_schedule_relation_time_period_1);
            tvo.setRelation_id(vo.getId());
            View view = listView.getChildAt(i);
            if(view==null){

                TraceUtil.traceLog(TAG+"updateViewsToList.view=null");
                continue;
            }
            List tmpList=new ArrayList();
            CheckBox cb = (CheckBox)view.findViewById(R.id.checkBox_week1);
            if(cb.isChecked()){
                tmpList.add("1");
            }
            cb = (CheckBox)view.findViewById(R.id.checkBox_week2);
            if(cb.isChecked()){
                tmpList.add("2");
            }
            cb = (CheckBox)view.findViewById(R.id.checkBox_week3);
            if(cb.isChecked()){
                tmpList.add("3");
            }
            cb = (CheckBox)view.findViewById(R.id.checkBox_week4);
            if(cb.isChecked()){
                tmpList.add("4");
            }
            cb = (CheckBox)view.findViewById(R.id.checkBox_week5);
            if(cb.isChecked()){
                tmpList.add("5");
            }
            cb = (CheckBox)view.findViewById(R.id.checkBox_week6);
            if(cb.isChecked()){
                tmpList.add("6");
            }
            cb = (CheckBox)view.findViewById(R.id.checkBox_week7);
            if(cb.isChecked()){
                tmpList.add("7");
            }
            tvo.setDays(StringUtils.join(tmpList,","));
            tvo.setStart_time (((TextView)view.findViewById(R.id.start_time)).getText().toString());
            tvo.setEnd_time (((TextView)view.findViewById(R.id.end_time)).getText().toString());

        }

        }catch (Exception e){
            TraceUtil.traceThrowableLog(e);
            DialogUtils.alertErrMsg(mContext, TAG + "onPostExecute:" + e.getMessage());
            return false;
        }

        return true;
    }
    @Override
    public void finish() {
        Intent intent = new Intent(mContext, MyTrainingCourseActivity.class);
        startActivity(intent);
        super.finish();
    }

    private class SaveAsyncTask extends AbstractAsyncTask<String, Void, Object> {

        private TrainingCourseJsonform form;

        protected SaveAsyncTask(TrainingCourseJsonform form, TrainingCourseDetailActivity trainingCourseEditActivity) {
            this.form = form;
            this.trainingCourseEditActivity=trainingCourseEditActivity;
        }

        private TrainingCourseDetailActivity trainingCourseEditActivity;

        @Override
        protected Object doInBackground(String[] params) {
            String url="rest/trainingCourse/save.json";
            DefaultRequest request= new DefaultRequest(mContext,url,Constant.RequestCode.REQUEST_POST);
            request.setData(form);
            HttpReturn httpReturn = HttpControl.execute(request, mContext);
            IResponse response = new IResponse();
            return response.parseFrom(httpReturn);
            //保存是否保存密码的选项
           // return CategoryDataProvider.getInstance().register(mContext, form);
        }

        @Override
        protected void onPostExecute(Object result) {
            try{


            dismissProgress();
            super.onPostExecute(result);
            if(result == null) {
                DialogUtils.alertErrMsg(mContext,"服务器未知错误!");
            } else {
                BaseResultEntity responseEntity = (BaseResultEntity) result;

                if (Constant.ResponseData.STATUS_SUCCESS.equals(responseEntity.getStatus()) ){
                    Toast.makeText(mContext, responseEntity.getResultMsg(), Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = (JSONObject) responseEntity.getResultObject();
                    String data_id = jsonObject.optString(Constant.ResponseData.DATA_ID);
                    vo.setId(Long.valueOf(data_id));
                    //先保存课程，在保存关联时段


                  //  Intent intent = new Intent(mContext, MyTrainingCourseActivity.class);
                  //  startActivity(intent);
                  //  finish();
                }else{
                    DialogUtils.alertErrMsg(mContext,responseEntity.getResultMsg());
                }
            }
            } catch (Exception e) {
                Log.e("TAG",e.getMessage());
                TraceUtil.traceThrowableLog(e);
                DialogUtils.alertErrMsg(mContext,e.getMessage());
                DialogUtils.alertErrMsg(mContext,TAG+ " onPostExecute:" + e.getMessage());
            }
        }

    }


    /**
     * 查询训练计划
     */
    private class TimeScheduleRelationQueryAsyncTask extends AbstractAsyncTask<String, Void, Object> {
        private Long relation_id;
        private Integer type;
        private Context context;
        private TimeScheduleRelationSearchContion nSearchContion=new TimeScheduleRelationSearchContion();
      //  private int operate;

        public TimeScheduleRelationQueryAsyncTask(Context context,String type,Long relation_id) {
            this.context = context;
            nSearchContion.setRelation_id(relation_id);
            nSearchContion.setType(type);
        }
        @Override
        protected Object doInBackground(String[] params) {

            String url = "rest/timeScheduleRelation/query.json";
            DefaultRequest request = new DefaultRequest(mContext, url, Constant.RequestCode.REQUEST_GET);
            request.setParaObj(nSearchContion);
            HttpReturn httpReturn = HttpControl.execute(request, mContext);
            IResponse response = new IResponse();
            return response.parseFrom(httpReturn);
        }

        @Override
        protected void onPostExecute(Object result) {
            //恢复按钮文字为登录



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
                List<TimeScheduleRelationVO> list = gson.fromJson(dataArrStr, new TypeToken<List<TimeScheduleRelationVO>>() {
                }.getType());
                baseListAdapter.setList(list);

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
