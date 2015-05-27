package com.company.runman.comman.application;

import com.baidu.frontia.FrontiaApplication;
import com.company.runman.datacenter.model.AccountEntity;
import com.company.runman.utils.TraceUtil;

/**
 * Created by Edison on 14-7-22.
 */
public class SobeyApplication extends FrontiaApplication {

    private static final String TAG = "SobeyApplication";
    private AccountEntity accountEntity = new AccountEntity();

    @Override
    public void onCreate() {
        super.onCreate();
        TraceUtil.setUncaughtExceptionHandler();
    }

    public AccountEntity getAccountEntity() {
        return accountEntity;
    }

    public void setAccountEntity(AccountEntity accountEntity) {
        this.accountEntity = accountEntity;
    }
}
