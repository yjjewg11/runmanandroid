package com.company.runman.datacenter.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.company.runman.datacenter.database.dao.ICodeScanHistoryDao;
import com.company.runman.datacenter.database.dao.IMonitorDao;
import com.company.runman.datacenter.database.dao.ISearchDao;
import com.company.runman.datacenter.database.dao.IWebPageDao;
import com.company.runman.datacenter.database.dao.impl.CodeScanHistoryDaoImpl;
import com.company.runman.datacenter.database.dao.impl.MonitorStatsAlertRequestDaoImpl;
import com.company.runman.datacenter.database.dao.impl.SearchDaoImpl;
import com.company.runman.datacenter.database.dao.impl.WebPageDaoImpl;
import com.company.runman.datacenter.model.*;
import com.company.runman.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EdisonZhao on 14-8-5.
 * Email:zhaoliangyu@sobey.com
 */
public class DBHelper {

    private static final String TAG = "DBHelper";
    private static DBHelper dbHelper;

    public static DBHelper getInstance() {
        if (dbHelper == null) {
            dbHelper = new DBHelper();
        }
        return dbHelper;
    }

    /**
     * 收藏网页
     *
     * @param webPageEntity
     * @param context
     * @return
     */
    public boolean insertWebPageToDB(WebPageEntity webPageEntity, Context context) {
        try {
            SQLiteDatabase db = DBUtil.openDatabase(context);
            IWebPageDao webPageDao = new WebPageDaoImpl();
            webPageDao.insertWebPage(webPageEntity, db);
        } catch (Exception e) {
            LogHelper.d(TAG, "", e);
            return false;
        } finally {
            DBUtil.closeDatabase();
        }
        return true;
    }

    /**
     * 是否已经存在该收藏网页
     *
     * @return
     */
    public boolean isWebPageExist(WebPageEntity webPageEntity, Context context) {
        try {
            SQLiteDatabase db = DBUtil.openDatabase(context);
            IWebPageDao webPageDao = new WebPageDaoImpl();
            return webPageDao.queryWebPage(webPageEntity, db);
        } catch (Exception e) {
            LogHelper.d(TAG, "", e);
            return false;
        } finally {
            DBUtil.closeDatabase();
        }
    }

    /**
     * 获得所有收藏的网页
     *
     * @param context
     * @return
     */
    public List<WebPageEntity> getAllCollectionWebPages(Context context) {
        List<WebPageEntity> webPageEntityList = new ArrayList<WebPageEntity>();
        try {
            SQLiteDatabase db = DBUtil.openDatabase(context);
            IWebPageDao webPageDao = new WebPageDaoImpl();
            webPageEntityList = webPageDao.getAllWebPages(db);
        } catch (Exception e) {
            LogHelper.d(TAG, "", e);
        } finally {
            DBUtil.closeDatabase();
        }
        return webPageEntityList;
    }

    /**
     * 删除收藏网页
     *
     * @param webPageEntity
     * @param context
     * @return
     */
    public boolean deleteCollectionWebPage(WebPageEntity webPageEntity, Context context) {
        try {
            SQLiteDatabase db = DBUtil.openDatabase(context);
            IWebPageDao webPageDao = new WebPageDaoImpl();
            webPageDao.deleteWebPage(webPageEntity, db);
        } catch (Exception e) {
            LogHelper.d(TAG, "", e);
            return false;
        } finally {
            DBUtil.closeDatabase();
        }
        return true;
    }

    /**
     * 保存轮询请求报警信息
     *
     * @param context
     * @return
     */
    public boolean insertMonitorStatsAlertListRequestDB(String userCode, MonitorStatsEventDetailEntity data, Context context) {
        try {
            SQLiteDatabase db = DBUtil.openDatabase(context);
            IMonitorDao monitorDao = new MonitorStatsAlertRequestDaoImpl();
            ContentValues contentValues = new ContentValues();
            contentValues.put("Time", data.getTime());
            contentValues.put("StationCode", data.getGroupCode());
            contentValues.put("UserCode", userCode);
            contentValues.put("LastAlertTime", data.getLastAlertTime());
            contentValues.put("IsReviewed", 0);
            contentValues.put("KitCount", data.getKitCount());
            MonitorStatsAlertDBData oldData = (MonitorStatsAlertDBData) monitorDao.queryMonitorData(contentValues, db);
            if (oldData != null) {
                //如果存在,直接更新
                monitorDao.updateMonitorData(contentValues, db);
            } else {
                monitorDao.insertMonitorData(contentValues, db);
            }
        } catch (Exception e) {
            LogHelper.d(TAG, "", e);
            return false;
        } finally {
            DBUtil.closeDatabase();
        }
        return true;
    }

    /**
     * 获取轮询请求报警信息
     *
     * @param context
     * @return
     */
    public List<MonitorStatsAlertDBData> queryMonitorStatsAlertListRequestDB(String userCode, Context context) {
        try {
            SQLiteDatabase db = DBUtil.openDatabase(context);
            IMonitorDao monitorDao = new MonitorStatsAlertRequestDaoImpl();
            ContentValues contentValues = new ContentValues();
            contentValues.put("UserCode", userCode);
            List<MonitorStatsAlertDBData> list = (List<MonitorStatsAlertDBData>) monitorDao.queryMonitorDataList(contentValues, db);
            return list;
        } catch (Exception e) {
            LogHelper.d(TAG, "", e);
            return new ArrayList<MonitorStatsAlertDBData>();
        } finally {
            DBUtil.closeDatabase();
        }
    }

    /**
     * 获取轮询请求报警信息
     *
     * @param context
     * @return
     */
    public void updateMonitorStatsAlertListRequestDB(String userCode, String stationCode, Context context) {
        try {
            SQLiteDatabase db = DBUtil.openDatabase(context);
            IMonitorDao monitorDao = new MonitorStatsAlertRequestDaoImpl();
            ContentValues contentValues = new ContentValues();
            contentValues.put("UserCode", userCode);
            contentValues.put("StationCode", stationCode);
//            MonitorStatsAlertDBData data = (MonitorStatsAlertDBData) monitorDao.queryMonitorData(contentValues, db);
            contentValues.put("IsReviewed", 1);
            monitorDao.updateMonitorData(contentValues, db);
        } catch (Exception e) {
            LogHelper.d(TAG, "", e);
        } finally {
            DBUtil.closeDatabase();
        }
    }

    /**
     * 查询所有本地保存的关键词查询
     *
     * @param context
     * @param userCode
     * @return
     */
    public List<? extends Object> queryAllKeywordFromDB(Context context, String userCode) {
        try {
            SQLiteDatabase db = DBUtil.openDatabase(context);
            ISearchDao searchDao = new SearchDaoImpl();
            return searchDao.queryKeywordDataList(userCode, db);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeDatabase();
        }
        return null;
    }

    /**
     * 插入新的关键词
     *
     * @param context
     * @param keywordEntity
     */
    public void insertKeywordFromDB(Context context, KeywordEntity keywordEntity) {
        try {
            SQLiteDatabase db = DBUtil.openDatabase(context);
            ISearchDao searchDao = new SearchDaoImpl();
            //先查询是否有同一条数据
            Object object = searchDao.getKeywordData(keywordEntity, db);
            if (null != object) {
                //存在同一条数据,就刷新该数据
                searchDao.updateKeywordData((KeywordEntity) object, db);
            } else {
                //查询是否满足10条
                List<? extends Object> list = searchDao.queryKeywordDataList(keywordEntity.getUserCode(), db);
                if (list.size() >= DBUtil.DBStatus.KEYWORD_MAX_COUNT) {
                    //如果超过或者等于最大次数,删除最旧得那条数据
                    KeywordEntity oldKeyword = (KeywordEntity) list.get(list.size() - 1);
                    searchDao.deleteKeywordDataById(oldKeyword, db);
                }
                //插入新的数据
                searchDao.insertKeywordData(keywordEntity, db);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeDatabase();
        }
    }

    /**
     * 根据ID删除关键词
     *
     * @param context
     * @param keywordEntity
     */
    public void deleteKeywordFromDB(Context context, KeywordEntity keywordEntity) {
        try {
            SQLiteDatabase db = DBUtil.openDatabase(context);
            ISearchDao searchDao = new SearchDaoImpl();
            searchDao.deleteKeywordDataById(keywordEntity, db);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeDatabase();
        }
    }

    /**
     * 插入新的关键词
     *
     * @param context
     * @param keywordEntity
     */
    public void updateKeywordFromDB(Context context, KeywordEntity keywordEntity) {
        try {
            SQLiteDatabase db = DBUtil.openDatabase(context);
            ISearchDao searchDao = new SearchDaoImpl();
            searchDao.updateKeywordData(keywordEntity, db);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeDatabase();
        }
    }

    /**
     * 插入新的扫一扫记录
     *
     * @param context
     * @param codeScanHistoryEntity
     * @return
     */
    public void insertCodeScanHistoryToDB(Context context, CodeScanHistoryEntity codeScanHistoryEntity) {
        try {
            SQLiteDatabase db = DBUtil.openDatabase(context);
            ICodeScanHistoryDao codeScanHistoryDao = new CodeScanHistoryDaoImpl();
            codeScanHistoryDao.insertCodeScanHistory(codeScanHistoryEntity, db);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeDatabase();
        }
    }

    /**
     * 查询所有扫一扫记录
     *
     * @param context
     * @param userCode
     * @return
     */
    public List<? extends Object> queryAllCodeScanHistoryFromDB(Context context, String userCode) {
        try {
            SQLiteDatabase db = DBUtil.openDatabase(context);
            ICodeScanHistoryDao codeScanHistoryDao = new CodeScanHistoryDaoImpl();
            return codeScanHistoryDao.getAllCodeScanHistorys(db, userCode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeDatabase();
        }
        return null;
    }

    /**
     * 删除扫一扫记录
     *
     * @param context
     * @param ids
     */
    public void deleteCodeScanHistoryFromDB(Context context, List<Integer> ids) {
        try {
            SQLiteDatabase db = DBUtil.openDatabase(context);
            ICodeScanHistoryDao codeScanHistoryDao = new CodeScanHistoryDaoImpl();
            codeScanHistoryDao.deleteCodeScanHistorys(ids, db);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeDatabase();
        }
    }
}
