package com.company.runman.datacenter.database.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.company.runman.datacenter.database.DBUtil;
import com.company.runman.datacenter.database.dao.IMonitorDao;
import com.company.runman.datacenter.model.MonitorStatsAlertDBData;
import com.company.runman.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EdisonZhao on 14-10-23.
 * Email:zhaoliangyu@sobey.com
 */
public class MonitorStatsAlertRequestDaoImpl implements IMonitorDao {

    private static final String TABLE_NAME = "monitor_stats_alert_request";

    @Override
    public void insertMonitorData(ContentValues contentValues, SQLiteDatabase db) {
        try {
            db.insert(TABLE_NAME, "", contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateMonitorData(ContentValues contentValues, SQLiteDatabase db) {
        try {
            String[] strings = new String[]{contentValues.getAsString("UserCode"), contentValues.getAsString("StationCode")};
            db.update(TABLE_NAME, contentValues, "UserCode=? and StationCode=?", strings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMonitorData(ContentValues contentValues, SQLiteDatabase db) {

    }

    @Override
    public List<MonitorStatsAlertDBData> getMonitorDataList(SQLiteDatabase db) {
        return null;
    }

    @Override
    public MonitorStatsAlertDBData queryMonitorData(ContentValues contentValues, SQLiteDatabase db) {
        String sql = "SELECT *  FROM " + TABLE_NAME
                + " where UserCode='" + contentValues.getAsString("UserCode") + "'"
                + " and StationCode='" + contentValues.getAsString("StationCode") + "'";
        Cursor sc = null;
        try {
            sc = db.rawQuery(sql, null);
            if (sc.getCount() > 0) {
                sc.moveToFirst();
                MonitorStatsAlertDBData data = new MonitorStatsAlertDBData();
                data.setLastAlertTime(sc.getLong(sc.getColumnIndex("LastAlertTime")));
                data.setTime(sc.getLong(sc.getColumnIndex("Time")));
                data.setStationCode(sc.getString(sc.getColumnIndex("StationCode")));
                data.setKitCount(sc.getInt(sc.getColumnIndex("KitCount")));
                data.setUserCode(sc.getString(sc.getColumnIndex("UserCode")));
                data.setIsReviewed(sc.getInt(sc.getColumnIndex("IsReviewed")));
                return data;
            }
        } catch (Exception e) {
            LogHelper.d(TAG, "", e);
        } finally {
            if (sc != null && !sc.isClosed()) {
                sc.close();
            }
        }
        return null;
    }

    @Override
    public List<MonitorStatsAlertDBData> queryMonitorDataList(ContentValues contentValues, SQLiteDatabase db) {
        String sql = "SELECT *  FROM " + TABLE_NAME
                + " where UserCode='" + contentValues.getAsString("UserCode") + "'";
        Cursor sc = null;
        List<MonitorStatsAlertDBData> dataList = new ArrayList<MonitorStatsAlertDBData>();
        try {
            sc = db.rawQuery(sql, null);
            if (sc.getCount() > 0) {
                while (sc.moveToNext()) {
                    MonitorStatsAlertDBData data = new MonitorStatsAlertDBData();
                    data.setLastAlertTime(sc.getLong(sc.getColumnIndex("LastAlertTime")));
                    data.setTime(sc.getLong(sc.getColumnIndex("Time")));
                    data.setStationCode(sc.getString(sc.getColumnIndex("StationCode")));
                    data.setKitCount(sc.getInt(sc.getColumnIndex("KitCount")));
                    data.setUserCode(sc.getString(sc.getColumnIndex("UserCode")));
                    data.setIsReviewed(sc.getInt(sc.getColumnIndex("IsReviewed")));
                    dataList.add(data);
                }
                return dataList;
            }
        } catch (Exception e) {
            LogHelper.d(TAG, "", e);
        } finally {
            if (sc != null && !sc.isClosed()) {
                sc.close();
            }
        }
        return dataList;
    }

}
