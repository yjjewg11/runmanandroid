package com.company.runman.net.interfaces;

import android.content.Context;
import com.company.news.query.PaginationData;
import com.company.runman.utils.GsonUtils;
import com.company.runman.utils.InvokeUtils;
import com.google.gson.Gson;
import com.company.runman.datacenter.provider.SharePreferenceProvider;
import com.company.runman.utils.Constant;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edison on 2014/6/6.
 */
public abstract class IRequest {
   static public  GsonUtils gsonUtils=new GsonUtils();
    static  protected boolean isSuccess;
    protected String reuqestUrl;
    protected int httpMode;

    public int getHttpMode() {
        return httpMode;
    }
    private Object paraObj;
    private Object formObj;
    public static final String TAG = "IRequest";
    public static final String ZIPPrefix = "GZIP:";
    public String accessToken = "";

    public IRequest(Context context) {
       // accessToken = SharePreferenceProvider.getInstance(context).getAccessToken();
    }

    public Object getParaObj() {
        return paraObj;
    }

    public void setParaObj(Object paraObj) {
        this.paraObj = paraObj;
    }

    public  void setData(Object T){
        formObj=T;
    }

    public  Object getData(){
        return formObj;
    }

    /**
     * 根据form对象生成，url后面参数
     * @return
     */
    public String getParmString() {
        StringBuffer sb = new StringBuffer();
        if( this.getParaObj()==null)return null;
        List tmpParmList=new ArrayList();
        InvokeUtils.objectAttSToStringList(this.getParaObj(),tmpParmList,null);
        if(tmpParmList.size()==0){
            return null;
        }
        String s= StringUtils.join(tmpParmList, "&");
        return s;
    }



    public String getUrl(){
        StringBuffer sb = new StringBuffer();
        String param=getParmString();
        sb.append(Constant.Host.BASE_HOST).append(reuqestUrl);
        if(param==null)return sb.toString();
        return sb.append("?").append(param).toString();
    }

    public String getPost() {
        String jsonstr="{}";
        if(this.getData()!=null){
            jsonstr= gsonUtils.toJson(this.getData());
        }
        android.util.Log.i(TAG,"getPost="+jsonstr);
        return jsonstr;
    }
    public static String getHost(){
        return Constant.Host.BASE_HOST;
    }

}
