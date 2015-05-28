package com.company.runman.asynctask;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.company.news.query.NSearchContion;
import com.company.news.vo.TimeScheduleRelationVO;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.common.StringUtils;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/5/28.
 */
public abstract class AbstractDetailTrainingCourseAsyncTask extends AbstractAsyncTask<String, Void, Object> {
    static public String TAG="MyTrainingCourseActivity";
    private String id;
    Context mContext;


    public abstract    void onPostExecute2(TrainingCourseVO vo);

    public AbstractDetailTrainingCourseAsyncTask(Context mContext, String id) {
        this.id = id;
    }

    @Override
    protected Object doInBackground(String[] params) {

        String url = "rest/trainingCourse/{uuid}.json";
        url = url.replace("{uuid}", id);
        DefaultRequest request = new DefaultRequest(mContext, url, Constant.RequestCode.REQUEST_GET);

        HttpReturn httpReturn = HttpControl.execute(request, mContext);

        IResponse response = new IResponse();
        return response.parseFrom(httpReturn);
    }

    @Override
    protected void onPostExecute(Object result) {
        if(mContext instanceof  BaseActivity)
            ((BaseActivity)mContext).dismissProgress();
        try {
            super.onPostExecute(result);
            BaseResultEntity resultEntity = (BaseResultEntity) result;
            if (!Constant.ResponseData.STATUS_SUCCESS.equals(resultEntity.getStatus())) {
                DialogUtils.alertErrMsg(mContext, resultEntity.getResultMsg());
                return;
            }
            JSONObject jsonObject = (JSONObject) resultEntity.getResultObject();
            String dataArrStr = jsonObject.optString(Constant.ResponseData.DATA);

            String time_list = jsonObject.optString("time_list");

            TrainingCourseVO vo = (TrainingCourseVO) new GsonUtils().stringToObject(dataArrStr, TrainingCourseVO.class);
            Gson gson = new GsonUtils().getGson();
            if(time_list!=null){

                JsonParser parser = new JsonParser();
                JsonArray Jarray = parser.parse(time_list).getAsJsonArray();
                List<TimeScheduleRelationVO> tmpList  = new ArrayList<TimeScheduleRelationVO>();
                for(JsonElement obj : Jarray )
                {
                    TimeScheduleRelationVO cse = gson.fromJson( obj , TimeScheduleRelationVO.class);
                    tmpList.add(cse);
                }
                vo.setTime_list(tmpList);
                onPostExecute2(vo);
            }

//                IntentUtils.startTrainingCourseEditActivity(mContext, vo);
            IntentUtils.startTrainingCourseEditActivity(mContext, vo);
        } catch (Exception e) {
            TraceUtil.traceThrowableLog(e);
            DialogUtils.alertErrMsg(mContext,TAG+ "onPostExecute:" + e.getMessage());
        }
    }


}
