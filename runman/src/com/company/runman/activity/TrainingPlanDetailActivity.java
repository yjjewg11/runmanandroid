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
import com.company.runman.asynctask.AbstractPostAsyncTask;
import com.company.runman.asynctask.AbstractSaveAsyncTask;
import com.company.runman.datacenter.model.BaseResultEntity;
import com.company.runman.net.HttpControl;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.net.request.DefaultRequest;
import com.company.runman.utils.*;
import com.company.runman.widget.DialogUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

/**
 * Created by LMQ on 14-9-3.
 * Email:
 */
public class TrainingPlanDetailActivity extends BaseActivity {
    static private String ClassName="TrainingPlanDetailActivity";

    private TextView exercise_mode;
    private TextView start_date;
    private TextView start_to_end_time;
    private TextView status;
    private TextView place;
    private TextView runKM;
    private TextView run_times;
    private TextView price;
    private TrainingPlanVO vo;

    private Button button_modify;



    @Override
    public void initView() {
        setHeaderTitle("详细");
    }

    @Override
    public void initData() {
        Intent intent = this.getIntent();
         vo=(TrainingPlanVO)intent.getSerializableExtra(Constant.ResponseData.DATA);

        reloadData();
        
    }

    public  void reloadData(){
        if(vo==null){
            return;
        }
        exercise_mode=(TextView)findViewById(R.id.exercise_mode);
        String tmp="普通方式";
        if(Integer.valueOf(2).equals(vo.getExercise_mode())){
            tmp="马拉松";
        }
        exercise_mode.setText("运动方式："+tmp);
        runKM.setText("距离："+Tool.objectToString(vo.getRunKM()));
        run_times.setText("运动时间："+Tool.objectToString(vo.getRun_times()));
        price.setText("出价："+Tool.objectToString(vo.getPrice()));
        start_date.setText("开始时间："+TimeUtils.getDateString(vo.getStart_time()));
        start_to_end_time.setText(TimeUtils.getHourAndMinuteByDate(vo.getStart_time())+"-"+TimeUtils.getHourAndMinuteByDate(vo.getEnd_time()));
        //   end_time.setText(end_time.getText().toString()+TimeUtils.getHourAndMinuteByDate(vo.getEnd_time()));
        place.setText("地点："+Tool.objectToString(vo.getPlace()));
        status.setText("状态："+VOUtils.trainingPlanStatusToString(vo.getStatus()));
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
        status= (TextView) findViewById(R.id.status);
        findViewById(R.id.button_status_request).setOnClickListener(this);
        findViewById(R.id.button_status_pay).setOnClickListener(this);
        findViewById(R.id.button_status_complete).setOnClickListener(this);




    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        String url=null;
        switch (v.getId()) {
            case R.id.button_modify:
                IntentUtils.startTrainingPlanEditActivity(mContext, vo);
                break;
            case R.id.button_status_request:
                 url="rest/trainingPlan/status/request/{uuid}.json";
                url = url.replace("{uuid}", vo.getId().toString());
                new AbstractPostAsyncTask(null,TrainingPlanDetailActivity.this,url){
                    @Override
                    protected void onPostExecute2(JSONObject jsonObject) {

                        String jsonObjectStr = jsonObject.optString(Constant.ResponseData.DATA);
                        vo = (TrainingPlanVO) new GsonUtils().stringToObject(jsonObjectStr, TrainingPlanVO.class);
                        reloadData();
//                        Intent intent = new Intent(mContext, MyInfoActivity.class);
//                        startActivity(intent);

                    }
                }.execute();
                break;
            case R.id.button_status_pay:
                 url="rest/trainingPlan/status/pay/{uuid}.json";
                url = url.replace("{uuid}", vo.getId().toString());
                new AbstractPostAsyncTask(null,TrainingPlanDetailActivity.this,url){
                    @Override
                    protected void onPostExecute2(JSONObject jsonObject) {

                        String jsonObjectStr = jsonObject.optString(Constant.ResponseData.DATA);
                        vo = (TrainingPlanVO) new GsonUtils().stringToObject(jsonObjectStr, TrainingPlanVO.class);
                        reloadData();
//                        Intent intent = new Intent(mContext, MyInfoActivity.class);
//                        startActivity(intent);

                    }
                }.execute();
                break;
            case R.id.button_status_complete:
                url="rest/trainingPlan/status/complete/{uuid}.json";
                url = url.replace("{uuid}", vo.getId().toString());
                new AbstractPostAsyncTask(null,TrainingPlanDetailActivity.this,url){
                    @Override
                    protected void onPostExecute2(JSONObject jsonObject) {

                        String jsonObjectStr = jsonObject.optString(Constant.ResponseData.DATA);
                        vo = (TrainingPlanVO) new GsonUtils().stringToObject(jsonObjectStr, TrainingPlanVO.class);
                        reloadData();
//                        Intent intent = new Intent(mContext, MyInfoActivity.class);
//                        startActivity(intent);

                    }
                }.execute();
                break;

        }
    }

//    @Override
//    public void finish() {
//        Intent intent = new Intent(mContext, MyInfoActivity.class);
//        startActivity(intent);
//        super.finish();
//    }

}
