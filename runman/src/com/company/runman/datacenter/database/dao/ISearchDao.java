package com.company.runman.datacenter.database.dao;

import android.database.sqlite.SQLiteDatabase;
import com.company.runman.datacenter.model.KeywordEntity;

import java.util.List;

/**
 * Created by LMQ on 14-11-3.
 * Email:
 */
public interface ISearchDao {

    public void insertKeywordData(KeywordEntity keywordEntity, SQLiteDatabase db);

    public void updateKeywordData(KeywordEntity keywordEntity, SQLiteDatabase db);

    public List<? extends Object> queryKeywordDataList(String userCode, SQLiteDatabase db);

    public Object getKeywordData(KeywordEntity keywordEntity, SQLiteDatabase db);

    public void deleteKeywordDataById(KeywordEntity keywordEntity, SQLiteDatabase db);
}
