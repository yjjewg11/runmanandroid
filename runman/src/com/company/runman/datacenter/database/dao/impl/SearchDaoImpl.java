package com.company.runman.datacenter.database.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.company.runman.datacenter.database.DBUtil;
import com.company.runman.datacenter.database.dao.ISearchDao;
import com.company.runman.datacenter.model.KeywordEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EdisonZhao on 14-11-3.
 * Email:zhaoliangyu@sobey.com
 */
public class SearchDaoImpl implements ISearchDao {

    private static final String TABLE_NAME = "search_keyword_table";


    @Override
    public void insertKeywordData(KeywordEntity keywordEntity, SQLiteDatabase db) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("Count", keywordEntity.getCount());
            cv.put("Keyword", keywordEntity.getKeyword());
            cv.put("Timestamp", keywordEntity.getTimestamp());
            cv.put("UserCode", keywordEntity.getUserCode());
            db.insert(TABLE_NAME, "", cv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateKeywordData(KeywordEntity keywordEntity, SQLiteDatabase db) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("Count", "" + (keywordEntity.getCount() + 1));
            cv.put("Timestamp", keywordEntity.getTimestamp());
            cv.put("UserCode", keywordEntity.getUserCode());
            db.update(TABLE_NAME, cv, "_id = ?", new String[]{keywordEntity.get_id() + ""});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<? extends Object> queryKeywordDataList(String userCode, SQLiteDatabase db) {
        Cursor cursor = null;
        List<KeywordEntity> list = new ArrayList<KeywordEntity>();
        try {
            String sql = "select * from " + TABLE_NAME + " where UserCode=? order by Timestamp DESC";
            cursor = db.rawQuery(sql, new String[]{userCode});
            if (null != cursor) {
                while (cursor.moveToNext()) {
                    KeywordEntity keywordEntity = new KeywordEntity();
                    keywordEntity.setUserCode(userCode);
                    keywordEntity.setKeyword(cursor.getString(cursor.getColumnIndexOrThrow("Keyword")));
                    keywordEntity.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("Timestamp")));
                    keywordEntity.set_id(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
                    keywordEntity.setCount(cursor.getInt(cursor.getColumnIndexOrThrow("Count")));
                    list.add(keywordEntity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }

    @Override
    public Object getKeywordData(KeywordEntity keywordEntity, SQLiteDatabase db) {
        Cursor cursor = null;
        try {
            String sql = "select * from " + TABLE_NAME + " where UserCode='" + keywordEntity.getUserCode()
                    + "' and Keyword='" + keywordEntity.getKeyword() + "'";
            cursor = db.rawQuery(sql, null);
            if (null != cursor && cursor.moveToNext()) {
                keywordEntity.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("Timestamp")));
                keywordEntity.set_id(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
                keywordEntity.setCount(cursor.getInt(cursor.getColumnIndexOrThrow("Count")));
                return keywordEntity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    @Override
    public void deleteKeywordDataById(KeywordEntity keywordEntity, SQLiteDatabase db) {
        try {
            db.delete(TABLE_NAME, "_id = ?", new String[]{"" + keywordEntity.get_id()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
