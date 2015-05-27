package com.company.runman.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.company.news.jsonform.TrainingCourseJsonform;
import com.company.news.validate.CommonsValidate;
import com.company.news.vo.TrainingCourseVO;
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
public class TrainingCourseEditActivity extends BaseActivity {
    static private String ClassName="TrainingCourseEditActivity";

    private TrainingCourseJsonform form;
    private RadioButton exercise_mode1;
    private RadioButton exercise_mode2;
    private EditText title;
    private EditText time_length;
    private EditText difficulty_degree;
    private EditText place;
    private EditText context;
    private EditText price;
    private View save_btn;



    @Override
    public void initView() {
        setHeaderTitle("编辑");
    }

    @Override
    public void initData() {
        Intent intent = this.getIntent();
        TrainingCourseVO vo=(TrainingCourseVO)intent.getSerializableExtra(Constant.ResponseData.DATA);
        if(vo==null){
            vo=new TrainingCourseVO();
          
            vo.setExercise_mode(1);
        }else{
            form=new TrainingCourseJsonform();
            form.setId(vo.getId());
        }
        if(Integer.valueOf(2).equals(vo.getExercise_mode())){
            exercise_mode2.setChecked(true);
        }else{
            exercise_mode1.setChecked(true);
        }
        title.setText(Tool.objectToString(vo.getTitle()));
        time_length.setText(Tool.objectToString(vo.getTime_length()));
        price.setText(Tool.objectToString(vo.getPrice()));
        difficulty_degree.setText(Tool.objectToString(vo.getDifficulty_degree()));
       place.setText(Tool.objectToString(vo.getPlace()));
        context.setText(Tool.objectToString(vo.getContext()));
        

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.training_course_edit_layout);
        exercise_mode1 = (RadioButton) findViewById(R.id.exercise_mode1);
        exercise_mode2 = (RadioButton) findViewById(R.id.exercise_mode2);
        difficulty_degree = (EditText) findViewById(R.id.difficulty_degree);
        context = (EditText) findViewById(R.id.context);


        place = (EditText) findViewById(R.id.place);
        title = (EditText) findViewById(R.id.title);
        time_length = (EditText) findViewById(R.id.time_length);
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
            if(exercise_mode2.isChecked()){
                form.setExercise_mode(2);
            }else{
                form.setExercise_mode(1);
            }
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
            new SaveAsyncTask(form).execute("");
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent(mContext, MyTrainingCourseActivity.class);
        startActivity(intent);
        super.finish();
    }

    private class SaveAsyncTask extends AbstractAsyncTask<String, Void, Object> {

        private TrainingCourseJsonform form;

        protected SaveAsyncTask(TrainingCourseJsonform form) {
            this.form = form;
        }

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
            dismissProgress();
            super.onPostExecute(result);
            if(result == null) {
                DialogUtils.alertErrMsg(mContext,"服务器未知错误!");
            } else {
                BaseResultEntity responseEntity = (BaseResultEntity) result;

                if (Constant.ResponseData.STATUS_SUCCESS.equals(responseEntity.getStatus()) ){
                    Toast.makeText(mContext, responseEntity.getResultMsg(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, MyTrainingCourseActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    DialogUtils.alertErrMsg(mContext,responseEntity.getResultMsg());
                }
            }
        }
    }
}
