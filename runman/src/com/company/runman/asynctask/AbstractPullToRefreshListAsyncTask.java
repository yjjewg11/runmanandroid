package com.company.runman.asynctask;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.company.news.query.NSearchContion;
import com.company.news.vo.TrainingCourseVO;
import com.company.runman.R;
import com.company.runman.activity.Adapter.DefaultAdapter;
import com.company.runman.datacenter.model.BaseResultEntity;
import com.company.runman.net.HttpControl;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.net.request.DefaultRequest;
import com.company.runman.utils.AbstractAsyncTask;
import com.company.runman.utils.Constant;
import com.company.runman.utils.GsonUtils;
import com.company.runman.utils.TraceUtil;
import com.company.runman.widget.DialogUtils;
import com.company.runman.widget.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.common.StringUtils;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 查询抽象列表
 * Created by Administrator on 2015/5/28.
 */
public abstract class AbstractPullToRefreshListAsyncTask extends AbstractAsyncTask<String, Void, Object>

    {
        static String TAG="AbstractPullToRefreshListAsyncTask";
        private Context mContext;
        private int operate;
        private DefaultAdapter baseListAdapter;

        public static PullToRefreshListView listView;

        public abstract String getQueryUrl();

        public AbstractPullToRefreshListAsyncTask(Context context, Integer operate,PullToRefreshListView listView) {
           this.mContext = context;
           this.operate = operate;
           this.listView=listView;
            //listView.getAdapter(); 获取的是 android.widget.HeaderViewListAdapter cannot be cast to com.company.runman.activity.Adapter.DefaultAdapter
            baseListAdapter=(DefaultAdapter)listView.getDefaultAdapter();
         }

        @Override
        protected Object doInBackground(String[] params) {
        NSearchContion nSearchContion=baseListAdapter.getsSearchContion();
        if (operate == Constant.Query.Operate_LoadMore ) {
            nSearchContion.addPageNo();
        }else{
            nSearchContion.restPageNo();
        }
            if(org.apache.commons.lang.StringUtils.isBlank(getQueryUrl())){
                DialogUtils.alertErrMsg(mContext, "请求URL为空");
                return null;
            }
        DefaultRequest request = new DefaultRequest(mContext, getQueryUrl(), Constant.RequestCode.REQUEST_GET);
        request.setParaObj(nSearchContion);
        HttpReturn httpReturn = HttpControl.execute(request, mContext);
        IResponse response = new IResponse();
        return response.parseFrom(httpReturn);
    }

        @Override
        protected void onPostExecute(Object result) {
        //恢复按钮文字为登录

        if (operate ==Constant.Query.Operate_Refresh ) {

            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd-  HH:mm:ss");
            String date = format.format(new Date());
            // Call onRefreshComplete when the list has been refreshed.
            listView.onRefreshComplete(date);
        } else if (operate ==Constant.Query.Operate_LoadMore ) {
            // mListItems.addLast("显示更多");
            listView.onLoadMoreComplete();
        }
        try {
            super.onPostExecute(result);
            BaseResultEntity resultEntity = (BaseResultEntity) result;
            if (!Constant.ResponseData.STATUS_SUCCESS.equals(resultEntity.getStatus())) {
                DialogUtils.alertErrMsg(mContext, resultEntity.getResultMsg());
                return;
            }
            JSONObject jsonObject = (JSONObject) resultEntity.getResultObject();
            String   dataArrStr = jsonObject.optString(Constant.ResponseData.DATA);


            List list=jsonToList(dataArrStr);
            if (operate ==Constant.Query.Operate_LoadMore ) {
                baseListAdapter.addAll(list);
            }else{
                baseListAdapter.setList(list);
            }
            if(list.size()==0){
                Toast.makeText(mContext, R.string.empty_data_text, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(mContext, "加载"+list.size()+"条", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e(TAG, "onPostExecute:", e);
            TraceUtil.traceThrowableLog(e);
            DialogUtils.alertErrMsg(mContext,TAG+ "onPostExecute:" + e.getMessage());
        }
    }

        /**
         * 返回的json数组转换成List
         * @param dataArrStr
         * @return
         */
       public abstract List jsonToList(String dataArrStr);
}
