package com.company.runman.datacenter.database.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by LMQ on 14-10-23.
 * Email:
 */
public interface IMonitorDao {

    public static final String TAG = "";

    public void insertMonitorData(ContentValues contentValues, SQLiteDatabase db);

    public void updateMonitorData(ContentValues contentValues, SQLiteDatabase db);

    public void deleteMonitorData(ContentValues contentValues, SQLiteDatabase db);

    public List<? extends Object> getMonitorDataList(SQLiteDatabase db);

    public List<? extends Object> queryMonitorDataList(ContentValues contentValues, SQLiteDatabase db);

    public Object queryMonitorData(ContentValues contentValues, SQLiteDatabase db);
}
