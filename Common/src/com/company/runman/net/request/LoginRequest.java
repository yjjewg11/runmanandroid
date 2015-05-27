package com.company.runman.net.request;

import android.content.Context;
import com.company.news.form.UserLoginForm;
import com.company.runman.datacenter.model.AccountEntity;
import com.company.runman.datacenter.model.BaseResultEntity;
import com.company.runman.datacenter.provider.SharePreferenceProvider;
import com.company.runman.net.HttpReturn;
import com.company.runman.net.interfaces.IRequest;
import com.company.runman.net.interfaces.IResponse;
import com.company.runman.utils.Constant;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by star on 2014/6/6.
 */
public class LoginRequest extends IRequest {
  public  static void main(String[] str){
        LoginRequest t=new LoginRequest(null);
        UserLoginForm form=new UserLoginForm();
        form.setLoginname("12345");
        form.setPassword("12323");
        t.setData(form);
        System.out.println(t.getParmString());
    }
    private UserLoginForm accountEntity;
    private Context context;

    public LoginRequest(Context context) {
        super(context);
        this.context = context;
    }



    @Override
    public String getUrl() {
        StringBuffer sb = new StringBuffer();
        String api = "rest/userinfo/login.json";
        String loginHost = SharePreferenceProvider.getInstance(context).readData(Constant.Host.LOGIN_HOST_KEY);
        return sb.append(Constant.Host.BASE_HOST).append(api).append("?").append(getParmString()).toString();
    }



    @Override
    public int getHttpMode() {
        return Constant.RequestCode.REQUEST_POST;
    }

    public static final class LoginResponse extends IResponse {

        private BaseResultEntity resultEntity = new BaseResultEntity();

        @Override
        public Object parseFrom(HttpReturn httpReturn) {
            Object data = super.parseFrom(httpReturn);
            resultEntity.setResultCode(httpReturn.getCode());
            if (data != null) {
                resultEntity.setResultObject(data);
                return resultEntity;
            }
            return resultEntity;
        }
    }
}
