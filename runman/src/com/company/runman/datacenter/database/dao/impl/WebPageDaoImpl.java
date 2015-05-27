package com.company.runman.datacenter.database.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.company.runman.datacenter.database.DBUtil;
import com.company.runman.datacenter.database.dao.IWebPageDao;
import com.company.runman.datacenter.model.WebPageEntity;
import com.company.runman.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EdisonZhao on 14-8-5.
 * Email:zhaoliangyu@sobey.com
 */
public class WebPageDaoImpl implements IWebPageDao {

    private static final String TAG = "WebPageDaoImpl";
    private static final String TABLE_NAME = "web_pages";

    @Override
    public void insertWebPage(WebPageEntity webPageEntity, SQLiteDatabase db) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("title", webPageEntity.getTitle());
            cv.put("url", webPageEntity.getUrl());
            cv.put("userCode", webPageEntity.getUserCode());
            cv.put("collectionTime", webPageEntity.getCollectionTime());
            db.insert(TABLE_NAME, "", cv);
        } catch (Exception e) {
            LogHelper.d(TAG, "", e);
        }
    }

    @Override
    public void deleteWebPage(WebPageEntity webPageEntity, SQLiteDatabase db) {
        try {
            db.delete(TABLE_NAME, "url = ? and userCode = ?", new String[]{webPageEntity.getUrl(), webPageEntity.getUserCode()});
        } catch (Exception e) {
            LogHelper.d(TAG, "", e);
        }
    }

    @Override
    public List<WebPageEntity> getAllWebPages(SQLiteDatabase db) {
        String sql = "SELECT *  FROM " + TABLE_NAME + " order by collectionTime DESC";
        List<WebPageEntity> webPageEntityList = new ArrayList<WebPageEntity>();
        Cursor sc = null;
        try {
            sc = db.rawQuery(sql, null);
            if (sc != null && sc.getCount() > 0) {
                while (sc.moveToNext()) {
                    WebPageEntity webPageEntity = new WebPageEntity();
                    webPageEntity.setTitle(sc.getString(sc.getColumnIndex("title")));
                    webPageEntity.setUrl(sc.getString(sc.getColumnIndex("url")));
                    webPageEntity.setCollectionTime(Long.valueOf(sc.getString(sc.getColumnIndex("collectionTime"))));
                    webPageEntity.setUserCode(sc.getString(sc.getColumnIndex("userCode")));
                    webPageEntityList.add(webPageEntity);
                }
            }
        } catch (Exception e) {
            LogHelper.d(TAG, "", e);
        } finally {
            if (sc != null && !sc.isClosed()) {
                sc.close();
            }
        }
        return webPageEntityList;
    }

    @Override
    public boolean queryWebPage(WebPageEntity webPageEntity, SQLiteDatabase db) {
        String sql = "SELECT *  FROM " + TABLE_NAME
                + " where userCode='" + webPageEntity.getUserCode() + "' and url='" + webPageEntity.getUrl() + "'"
                + " order by collectionTime DESC";
        Cursor sc = null;
        try {
            sc = db.rawQuery(sql, null);
            if (sc.getCount() > 0) {
                return true;
            }
        } catch (Exception e) {
            LogHelper.d(TAG, "", e);
        } finally {
            if (sc != null && !sc.isClosed()) {
                sc.close();
            }
        }
        return false;
    }
}
