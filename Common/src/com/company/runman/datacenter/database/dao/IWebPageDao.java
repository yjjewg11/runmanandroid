package com.company.runman.datacenter.database.dao;

import android.database.sqlite.SQLiteDatabase;
import com.company.runman.datacenter.model.WebPageEntity;

import java.util.List;

/**
 * Created by EdisonZhao on 14-8-5.
 * Email:zhaoliangyu@sobey.com
 */
public interface IWebPageDao {

    public void insertWebPage(WebPageEntity webPageEntity, SQLiteDatabase db);

    public void deleteWebPage(WebPageEntity webPageEntity, SQLiteDatabase db);

    public List<WebPageEntity> getAllWebPages(SQLiteDatabase db);

    public boolean queryWebPage(WebPageEntity webPageEntity, SQLiteDatabase db);
}
