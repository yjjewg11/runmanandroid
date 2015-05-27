package com.company.runman.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import android.widget.TextView;
import com.company.news.jsonform.TrainingPlanJsonform;
import com.company.news.vo.TrainingPlanVO;
import com.company.runman.R;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.datacenter.model.BaseResultEntity;
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
public class TrainingPlanDetailActivity extends BaseActivity {
    static private String ClassName="TrainingPlanDetailActivity";

    private TextView exercise_mode;
    private TextView start_date;
    private TextView start_to_end_time;
    //private TextView end_time;
    private TextView place;
    private TextView runKM;
    private TextView run_times;
    private TextView price;
    private TrainingPlanVO vo;

    private Button button_modify;



    @Override
    public void initView() {
        setHeaderTitle("编辑");
    }

    @Override
    public void initData() {
        Intent intent = this.getIntent();
         vo=(TrainingPlanVO)intent.getSerializableExtra(Constant.ResponseData.DATA);
        if(vo==null){
            return;
        }
        exercise_mode=(TextView)findViewById(R.id.exercise_mode);
        String tmp="普通方式";
        if(Integer.valueOf(2).equals(vo.getExercise_mode())){
            tmp="马拉松";
        }
        exercise_mode.setText(exercise_mode.getText().toString()+exercise_mode.getText().toString()+tmp);
        runKM.setText(runKM.getText().toString()+Tool.objectToString(vo.getRunKM()));
        run_times.setText(run_times.getText().toString()+Tool.objectToString(vo.getRun_times()));
        price.setText(price.getText().toString()+Tool.objectToString(vo.getPrice()));
        start_date.setText(start_date.getText().toString()+TimeUtils.getDateString(vo.getStart_time()));
        start_to_end_time.setText(TimeUtils.getHourAndMinuteByDate(vo.getStart_time())+"-"+TimeUtils.getHourAndMinuteByDate(vo.getEnd_time()));
     //   end_time.setText(end_time.getText().toString()+TimeUtils.getHourAndMinuteByDate(vo.getEnd_time()));
        place.setText(place.getText().toString()+Tool.objectToString(vo.getPlace()));
        
        
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.training_plan_detail_layout);

        button_modify= (Button) findViewById(R.id.button_modify);
        button_modify.setOnClickListener(this);
        start_date = (TextView) findViewById(R.id.start_date);

        start_to_end_time=(TextView) findViewById(R.id.start_to_end_time);
        place = (TextView) findViewById(R.id.palce);
        runKM = (TextView) findViewById(R.id.runKM);
        run_times = (TextView) findViewById(R.id.run_times);
        price = (TextView) findViewById(R.id.price);

    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == button_modify) {
            IntentUtils.startTrainingPlanEditActivity(mContext,vo);
        }
    }

//    @Override
//    public void finish() {
//        Intent intent = new Intent(mContext, MyInfoActivity.class);
//        startActivity(intent);
//        super.finish();
//    }

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
                    DialogUtils.alertErrMsg(mContext,responseEntity.getResultMsg());
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
