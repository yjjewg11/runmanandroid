package com.company.runman.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.company.news.jsonform.TrainingPlanJsonform;
import com.company.news.jsonform.UserRegJsonform;
import com.company.news.validate.CommonsValidate;
import com.company.news.vo.TrainingPlanVO;
import com.company.runman.R;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.comman.model.TimeSelectedEntity;
import com.company.runman.datacenter.model.BaseResultEntity;
import com.company.runman.datacenter.provider.SharePreferenceProvider;
import com.company.runman.net.HttpControl;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.net.request.DefaultRequest;
import com.company.runman.utils.*;
import com.company.runman.widget.DialogUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Created by EdisonZhao on 14-9-3.
 * Email:zhaoliangyu@sobey.com
 */
public class TrainingPlanEditActivity extends BaseActivity {
    static private String ClassName="TrainingPlanEditActivity";

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
    private View save_btn;



    @Override
    public void initView() {
        setHeaderTitle("编辑");
    }

    @Override
    public void initData() {
        Intent intent = this.getIntent();
        TrainingPlanVO vo=(TrainingPlanVO)intent.getSerializableExtra(Constant.ResponseData.DATA);
        if(vo==null){
            vo=new TrainingPlanVO();
            vo.setStart_time(TimeUtils.getCurrentTimestamp());
            vo.setEnd_time(TimeUtils.getCurrentTimestamp());
            vo.setExercise_mode(1);
        }else{
            form=new TrainingPlanJsonform();
            form.setId(vo.getId());
        }
        if(Integer.valueOf(2).equals(vo.getExercise_mode())){
            exercise_mode2.setChecked(true);
        }else{
            exercise_mode1.setChecked(true);
        }
        runKM.setText(Tool.objectToString(vo.getRunKM()));
        run_times.setText(Tool.objectToString(vo.getRun_times()));
        price.setText(Tool.objectToString(vo.getPrice()));
         start_date.setText(TimeUtils.getDateString(vo.getStart_time()));
        start_time.setText(TimeUtils.getHourAndMinuteByDate(vo.getStart_time()));
         end_time.setText(TimeUtils.getHourAndMinuteByDate(vo.getEnd_time()));
       place.setText(Tool.objectToString(vo.getPlace()));

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.training_plan_edit_layout);
        exercise_mode1 = (RadioButton) findViewById(R.id.exercise_mode1);
        exercise_mode2 = (RadioButton) findViewById(R.id.exercise_mode2);
        start_date = (EditText) findViewById(R.id.start_date);
        DialogUtils.showDateDialogBind(start_date);

        start_time = (EditText) findViewById(R.id.start_time);
        DialogUtils.showTimeDialogBind(start_time);

        end_time = (EditText) findViewById(R.id.end_time);
        DialogUtils.showTimeDialogBind(end_time);

        place = (EditText) findViewById(R.id.place);
        runKM = (EditText) findViewById(R.id.runKM);
        run_times = (EditText) findViewById(R.id.run_times);
        price = (EditText) findViewById(R.id.price);

        save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener(this);

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
            }
            String start_date_Str=start_date.getText().toString();
            if(TextUtils.isEmpty(start_date_Str)){
                Toast.makeText(mContext, "开始日期不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!StringUtils.isNumeric(runKM.getText().toString())){
                Toast.makeText(mContext, "距离不是有效数据!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!StringUtils.isNumeric(run_times.getText().toString())){
                Toast.makeText(mContext, "运动时间不是有效数据!", Toast.LENGTH_SHORT).show();
                return;

            }


            if(!CommonsValidate.isDecimal(price.getText().toString())) {
                Toast.makeText(mContext, "价格不是有效数据!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(form==null){//创建
                form = new TrainingPlanJsonform();
            }
            if(exercise_mode2.isChecked()){
                form.setExercise_mode(2);
            }else{
                form.setExercise_mode(1);
            }
            try{

            form.setStart_time(TimeUtils.getTimestamp(start_date_Str, start_time.getText().toString()));
            form.setEnd_time(TimeUtils.getTimestamp(start_date_Str,end_time.getText().toString()));

            form.setPlace(place.getText().toString());
            if(!TextUtils.isEmpty(runKM.getText())){
                form.setRunKM(Tool.convertInteger(runKM.getText().toString()));
            }
            if(!TextUtils.isEmpty(run_times.getText())){
                form.setRun_times(Tool.convertInteger(run_times.getText().toString()));
            }

            if(!TextUtils.isEmpty(price.getText())){
                form.setPrice(Tool.convertDouble(price.getText().toString()));
            }

            }catch (Exception e){
                TraceUtil.traceLog(TAG+"::"+e.getMessage());
            }

            showProgress(getString(R.string.progress_text));
            new SaveAsyncTask(form).execute("");
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent(mContext, MyInfoActivity.class);
        startActivity(intent);
        super.finish();
    }

    private class SaveAsyncTask extends AbstractAsyncTask<String, Void, Object> {

        private TrainingPlanJsonform form;

        protected SaveAsyncTask(TrainingPlanJsonform form) {
            this.form = form;
        }

        @Override
        protected Object doInBackground(String[] params) {
            String url="rest/trainingPlan/save.json";
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
            dismissProgress();
            super.onPostExecute(result);
            if(result == null) {
                DialogUtils.alertErrMsg(mContext,"服务器未知错误!");
            } else {
                BaseResultEntity responseEntity = (BaseResultEntity) result;

                if (Constant.ResponseData.STATUS_SUCCESS.equals(responseEntity.getStatus()) ){
                    Toast.makeText(mContext, responseEntity.getResultMsg(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, MyInfoActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    DialogUtils.alertErrMsg(mContext,responseEntity.getResultMsg());
                }
            }
        }
    }
}
