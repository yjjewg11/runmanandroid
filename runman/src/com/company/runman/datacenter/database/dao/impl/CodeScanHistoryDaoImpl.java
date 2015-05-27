package com.company.runman.datacenter.database.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.company.runman.datacenter.database.DBUtil;
import com.company.runman.datacenter.database.dao.ICodeScanHistoryDao;
import com.company.runman.datacenter.model.CodeScanHistoryEntity;
import com.company.runman.utils.LogHelper;

public class CodeScanHistoryDaoImpl implements ICodeScanHistoryDao {

    private static final String TAG = "CodeScanDaoImpl";
    private static final String TABLE_NAME = "code_scan_history_table";

    @Override
    public void insertCodeScanHistory(CodeScanHistoryEntity codeScanHistoryEntity, SQLiteDatabase db) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("context", codeScanHistoryEntity.getContext());
            cv.put("time", codeScanHistoryEntity.getTime());
            cv.put("usercode", codeScanHistoryEntity.getUserCode());
            cv.put("date", codeScanHistoryEntity.getDate());
            db.insert(TABLE_NAME, "", cv);
        } catch (Exception e) {
            LogHelper.d(TAG, "", e);
        }
    }

    @Override
    public List<CodeScanHistoryEntity> getAllCodeScanHistorys(SQLiteDatabase db, String userCode) {
        String sql = "SELECT *  FROM " + TABLE_NAME + " where UserCode='" + userCode + "' order by _id DESC";
        List<CodeScanHistoryEntity> codeScanHistoryEntityList = new ArrayList<CodeScanHistoryEntity>();
        Cursor sc = null;
        try {
            sc = db.rawQuery(sql, null);
            while (sc.moveToNext()) {
                CodeScanHistoryEntity codeScanHistoryEntity = new CodeScanHistoryEntity();
                codeScanHistoryEntity.setContext(sc.getString(sc.getColumnIndex("Context")));
                codeScanHistoryEntity.setTime(sc.getString(sc.getColumnIndex("Time")));
                codeScanHistoryEntity.setDate(sc.getString(sc.getColumnIndex("Date")));
                codeScanHistoryEntity.set_id(sc.getInt(sc.getColumnIndex("_id")));
                codeScanHistoryEntityList.add(codeScanHistoryEntity);
            }
        } catch (Exception e) {
            LogHelper.d(TAG, "", e);
        } finally {
            if (sc != null && !sc.isClosed()) {
                sc.close();
            }
        }
        return codeScanHistoryEntityList;
    }

    @Override
    public void deleteCodeScanHistorys(List<Integer> ids, SQLiteDatabase db) {
        try {
            if (ids != null && ids.size() > 0) {
                String id = "";
                for (int i = 0; i < ids.size() - 2; i++) {
                    id = id + ids.get(i) + ",";
                }
                id = id + ids.get(ids.size() - 1);
                db.delete(TABLE_NAME, "_id in ( " + id + " )", null);
            }
        } catch (Exception e) {
            LogHelper.d(TAG, "", e);
        }
    }

}
