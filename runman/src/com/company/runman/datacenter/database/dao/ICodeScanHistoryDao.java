package com.company.runman.datacenter.database.dao;

import java.util.List;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.company.runman.datacenter.model.CodeScanHistoryEntity;

public interface ICodeScanHistoryDao {
    public void insertCodeScanHistory(CodeScanHistoryEntity codeScanHistoryEntity, SQLiteDatabase db);

    public List<CodeScanHistoryEntity> getAllCodeScanHistorys(SQLiteDatabase db, String userCode);

    public void deleteCodeScanHistorys(List<Integer> ids, SQLiteDatabase db);
}
